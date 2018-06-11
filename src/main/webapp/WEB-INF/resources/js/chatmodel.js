"use strict";

//For you: Just to understand what duck type means 
/*
var ChatMessageProcessor = function() {

	return {
		"supports": function(message) {

			var somethingToReturn = true;

			return somethingToReturn;

		}, 
		"processMessage": function(chatService, chatModel, message) {

			// Put some logic here

		}
	};

};
 */

var RegExBasedChatMessageProcessor = function(regex, delegate) {

	var result = null;

	return {
		"supports": function(message) {

			regex.lastIndex = 0;

			result = regex.exec(message);

			var t = result != null && result.length > 0;

			return t;

		}, 
		"processMessage": function(chatService, chatModel, message) {

			delegate(
					chatService, 
					chatModel, 
					result.slice(1)
			);

		}
	};

};

var UnHandledChatMessageProcessor = function() {

	return {
		"supports": function(message) {

			return true;

		}, 
		"processMessage": function(chatService, chatModel, message) {

			chatModel
			.addMessage(
					"Impossibile gestire il messaggio."
			);

		}
	};

};

var ChangeUsernameErrorMessageProcessor = function() {

	return {
		"supports": function(message) {

			return message.indexOf("/error") != -1 && message.indexOf("username") != -1;

		}, 
		"processMessage": function(chatService, chatModel, message) {

			chatModel
			.addMessage(
					"Il nome utente e' gia' utilizzato."
			);

			chatModel.setUsername(
					chatModel.getOldUsername()		
			);

		}
	};

};

/*
	I am not using inheritance but delegation with duck type.
	Inheritance is BAD, it makes the code more coupled. 
	You should always prefer delegation over inheritance! :)
	And I am using duck type because I am using no interface for all the processors
 */
var ChatMessageHandler = function(chatModel, chatMessageParser) {

	var successor = null;

	return {
		"setSuccessor": function(chatMessageHandler) {

			successor = chatMessageHandler;

		}, 
		"handleMessage": function(chatService, message) {

			if(chatMessageParser.supports(message)) {

				chatMessageParser.processMessage(chatService, chatModel, message);

			} else {

				successor.handleMessage(chatService, message);

			}

		}

	};

};

var ChatMessageHandlerChainBuilder = function() {

	var chatMessageHandlerList = [ ];

	return {
		"addChatMessageHandler": function(chatMessageHandler) {

			chatMessageHandlerList.push(chatMessageHandler);

			return this;

		}, 
		"build": function() {

			if(chatMessageHandlerList.length > 0) {

				chatMessageHandlerList
				.indexes()
				.slice(1)
				.reverse()
				.forEach(function(i) {

					chatMessageHandlerList[i-1]
					.setSuccessor(chatMessageHandlerList[i]);

				});

				return chatMessageHandlerList[0];

			}

			return null;

		}

	};

};

var SimpleChatMessageHandlerChainBuilder = function(chatModel, chatMessageHandlerChainBuilder) {

	chatMessageHandlerChainBuilder
	.addChatMessageHandler(
			ChatMessageHandler(
					chatModel, 
					RegExBasedChatMessageProcessor(
							/\/join ([a-z0-9]+)/ig, 
							JoinMessageProcessorDelegate
					)
			)
	)
	.addChatMessageHandler(
			ChatMessageHandler(
					chatModel, 
					RegExBasedChatMessageProcessor(
							/\/quit ([a-z0-9]+)/ig, 
							QuitMessageProcessorDelegate
					)
			)
	)
	.addChatMessageHandler(
			ChatMessageHandler(
					chatModel, 
					RegExBasedChatMessageProcessor(
							/\/message (.+) \/sender ([a-z0-9]+)/i,  
							ChatMessageProcessorDelegate
					)
			)
	)
	.addChatMessageHandler(
			ChatMessageHandler(
					chatModel, 
					RegExBasedChatMessageProcessor(
							/\/pvt (.+) \/sender ([a-z0-9]+) \/recipient ([a-z0-9]+)/i,  
							PvtMessageProcessorDelegate
					)
			)
	)
	.addChatMessageHandler(
			ChatMessageHandler(
					chatModel, 
					RegExBasedChatMessageProcessor(
							/\/username ([a-z0-9]+)/ig, 
							ChangeUsernameMessageProcessorDelegate
					)
			)
	)
	.addChatMessageHandler(
			ChatMessageHandler(
					chatModel, 
					RegExBasedChatMessageProcessor(
							/\/whoiam ([a-z0-9]+)/ig,  
							WhoIAmMessageProcessorDelegate
					)
			)
	)
	.addChatMessageHandler(
			ChatMessageHandler(
					chatModel, 
					RegExBasedChatMessageProcessor(
							/\/userlist (.+)/ig,  
							UserListMessageProcessorDelegate
					)
			)
	)
	.addChatMessageHandler(
			ChatMessageHandler(
					chatModel, 
					ChangeUsernameErrorMessageProcessor()
			)
	)
	.addChatMessageHandler(
			ChatMessageHandler(
					chatModel, 
					RegExBasedChatMessageProcessor(
							/\/error (.+)/ig, 
							ErrorMessageProcessorDelegate
					)
			)
	)
	.addChatMessageHandler(
			ChatMessageHandler(
					chatModel, 
					UnHandledChatMessageProcessor()
			)
	)
	;

	return {
		"addChatMessageHandler": function(chatMessageHandler) {

			chatMessageHandlerChainBuilder.addChatMessageHandler(chatMessageHandler);

			return this;

		}, 
		"build": function() {

			return chatMessageHandlerChainBuilder.build();

		}
	};

};

function JoinMessageProcessorDelegate(chatService, chatModel, data) {

	chatModel
	.addMessage(
			data[0] + " si e' unito alla chat."
	);

	chatService.pushUserListChatMessage();

}

function QuitMessageProcessorDelegate(chatService, chatModel, data) {

	chatModel
	.addMessage(
			data[0] + " e' uscito dalla chat."
	);

	chatService.pushUserListChatMessage();

}

function ChatMessageProcessorDelegate(chatService, chatModel, data) {

	chatModel
	.addMessage(
			data[1] + " scrive: " + data[0]
	);

}

function PvtMessageProcessorDelegate(chatService, chatModel, data) {

	console.log(data[1]);
	console.log(chatModel.getUsername());

	if(chatModel.getUsername() == data[1]) {

		chatModel
		.addMessage(
				data[1] + " pvt a " + data[2] + ": " + data[0]
		);	

	} else {

		chatModel
		.addMessage(
				data[2] + " pvt da " + data[1] + ": " + data[0]
		);		

	}

}

function ChangeUsernameMessageProcessorDelegate(chatService, chatModel, data) {

	chatModel.setOldUsername(data[0]);
	chatModel.setUsername(data[0]);

}

function WhoIAmMessageProcessorDelegate(chatService, chatModel, data) {

	chatModel.setUsername(data[0]);
	chatModel.setOldUsername(data[0]);

}

function UserListMessageProcessorDelegate(chatService, chatModel, data) {

	var userList = [ ];

	data[0].split(",").forEach(function(e, i, a) {

		userList.push(e.trim());

	});

	chatModel.clearUserList();

	userList.forEach(function(e, i, a) {

		chatModel.addUser(e);

	});

}

function ErrorMessageProcessorDelegate(chatService, chatModel, data) {

	chatModel
	.addMessage(
			"Il comando non e' valido."
	);

}

var ChatService = function(webSocket) {

	var chatMessageHandler = null;
	var closed = false;

	var chatService = {
			"pushChatMessage": function(chatMessage) {

				var messageList = [ ];

				var chatCommand = chatMessage
				.map(function(e) {

					var key = Object.keys(e)[0];
					var value = e[key];

					value = value != null ? value : "";

					return "/" + key + " " + value;

				})
				.join(" ")
				.trim();

				console.log(chatCommand);

				webSocket.send(
						chatCommand
				);

			}, 
			"isClosed": function() {

				return closed;

			}, 
			"setChatMessageHandler": function(c) {

				chatMessageHandler = c;

			}, 
			"close": function() {

				webSocket.onclose = function(event) {};

				webSocket.close();

			}
	};

	webSocket.onmessage = function(event) {

		if(chatMessageHandler != null) {

			chatMessageHandler(chatService, event.data);

		}

	};

	webSocket.onclose = function(event) {

		closed = true;

	};

	webSocket.onerror = function(event) {

		chatService.close();

	};

	return chatService;

};

var SimpleChatService = function(chatService, chatMessageHandler) {

	var simpleChatService = {
			"pushSendAllChatMessage": function(message) {

				var message = [
					{"message": message}
					];

				chatService.pushChatMessage(message);

			}, 
			"pushSendToChatMessage": function(sender, to, message) {

				var message = [
					{"pvt": message}, 
					{"sender": sender}, 
					{"recipient": to}
					];

				chatService.pushChatMessage(message);

			}, 
			"pushWhoIAmMessage": function() {

				var message = [
					{"whoiam": null}
					];

				chatService.pushChatMessage(message);

			}, 
			"pushUserListChatMessage": function() {

				var message = [
					{"userlist": null}
					];

				chatService.pushChatMessage(message);

			}, 
			"pushChangeUsernameChatMessage": function(username) {

				var message = [
					{"username": username}
					];

				chatService.pushChatMessage(message);

			}, 
			"isClosed": function() {

				return chatService.isClosed();

			}, 
			"close": function() {

				chatService.close();

			}
	};

	chatService.setChatMessageHandler(function(chatService, message) {

		chatMessageHandler.handleMessage(
				simpleChatService, 
				message
		);

	});

	return simpleChatService;

};

var WebSocketFactory = function(endpoint, callback) {

	var webSocket = new WebSocket(endpoint);

	webSocket.onopen = function(event) {

		callback(webSocket, null);

	};

	webSocket.onerror = function(event) {

		var error = event;

		callback(null, error);

	};

};

function ChatModel() {

	return {
		"oldUsername": null, 
		"username": null, 
		"message": null, 
		"userList": [], 
		"messageList": [], 
		"chatting": false
	};

}

function ChatModel($scope) {

	var chatModel = {
			"getOldUsername": function() {

				return $scope.oldUsername;

			}, 
			"setOldUsername": function(u) {

				$scope.oldUsername = u;

			}, 
			"getUsername": function() {

				return $scope.username;

			}, 
			"setUsername": function(u) {

				$scope.username = u;

				$scope.$apply();

			}, 
			"getMessageToSend": function() {

				return $scope.message;

			}, 
			"setMessageToSend": function(message) {

				$scope.message = message;

			}, 
			"getUserList": function() {

				return $scope.userList.slice();

			},
			"addUser": function(user) {

				$scope.userList.push(user);

				$scope.$apply();

			},
			"clearUserList": function() {

				$scope.userList = [];

				$scope.$apply();

			}, 
			"getMessageList": function() {

				return $scope.messageList.slice();

			}, 
			"addMessage": function(message) {

				$scope.messageList.push(
						{
							"id": Math.floor((Math.random() * 1000000) + 1), 
							"message": DateBasedChatMessageFormatter(message)
						}
				);

				$scope.$apply();

			}, 
			"clearMessageList": function() {

				$scope.messageList = [];

			}, 
			"getChatting": function() {

				return $scope.chatting;

			}, 
			"setChatting": function(c) {

				$scope.chatting = c;

			}
	};

	return chatModel;

}

var DateBasedChatMessageFormatter = function(message) {

	var date = new Date();

	var AddZeroPadding = function(val) {

		val = val < 10 ? "0" + val : val;

		return val;

	};

	var day = AddZeroPadding(date.getDate());
	var month = AddZeroPadding(date.getMonth() + 1);
	var hours = AddZeroPadding(date.getHours());
	var minutes = AddZeroPadding(date.getMinutes());
	var seconds = AddZeroPadding(date.getSeconds()); 

	var messagePartList = [ ];
	messagePartList.push(day);
	messagePartList.push("/");
	messagePartList.push(month);
	messagePartList.push(" ");
	messagePartList.push(hours);
	messagePartList.push(":");
	messagePartList.push(minutes);
	messagePartList.push(":");
	messagePartList.push(seconds);
	messagePartList.push(" > ");
	messagePartList.push(message);

	return messagePartList.join("");

};