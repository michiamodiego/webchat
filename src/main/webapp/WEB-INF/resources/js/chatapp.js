"use strict"; 

var app = angular.module('chatApp', []);

app.directive('cEnterKeyDown', function() {
	return function(scope, element, attrs) {

		element.bind("keydown keypress", function(event) {
			var keyCode = event.which || event.keyCode;

			if (keyCode === 13) {

				scope.$apply(function() {

					scope.$eval(attrs.cEnterKeyDown);

				});

				event.preventDefault();

			}
		});

	};

});

app.constant("END_POINT", "ws://localhost:8080/webchat/chat");

app.service("chatServiceFactory", function(END_POINT, $q) {

	return {
		"getInstance": function(chatModel) {

			var deferred = $q.defer();

			WebSocketFactory(END_POINT, function(webSocket, e) {

				if(e == null) {

					deferred.resolve(
							SimpleChatService(
									ChatService(webSocket), 
									SimpleChatMessageHandlerChainBuilder(
											chatModel, 
											ChatMessageHandlerChainBuilder()
									).build()
							)
					);

				} else {

					var error = e;

					deferred.reject(error);

				}

			});

			return deferred.promise;

		}
	};

});

app.controller('chatController', function($rootScope, $scope, $interval, chatServiceFactory) {

	// There is just one controller and it is ok to put everything on the scope. 
	// This project is just an example.
	// The scope already represents the model
	$scope.oldUsername = null;
	$scope.username = null;
	$scope.message = null;
	$scope.messageList = [ ];
	$scope.userList = [ ];
	$scope.chatting = false;
	$scope.scrolling = true;

	var chatModel = ChatModel($scope);

	$scope.connect = function() {

		chatServiceFactory
		.getInstance(chatModel)
		.then(
				function(simpleChatService) {

					chatModel.setChatting(true);

					simpleChatService.pushWhoIAmMessage();
					simpleChatService.pushUserListChatMessage();

					$scope.changeUsername = function() {

						if(
								chatModel.getUsername().trim() != "" && 
								chatModel.getUsername() != chatModel.getOldUsername()
						) {

							simpleChatService
							.pushChangeUsernameChatMessage(
									chatModel.getUsername()
							);

						}

					};

					$scope.send = function() {

						var message = chatModel.getMessageToSend();

						if(message.trim() == "") {

							return;

						}

						var regexp = /\@([a-z0-9]+) (.+)/ig;
						var result = regexp.exec(message);

						if(result != null && result.length > 0) {

							var to = result[1];
							var message = result[2];

							simpleChatService.pushSendToChatMessage(
									chatModel.getUsername(), 
									to, 
									message
							);

						} else {

							simpleChatService.pushSendAllChatMessage(message);

						}

						chatModel.setMessageToSend("");

					};

					$scope.sendPvt = function(username) {

						if(username != chatModel.getUsername()) {

							chatModel.setMessageToSend("@" + username + " ");

						}

					};

					$interval(function() {

						if($scope.scrolling) {

							$("#messageListPanel").scrollTop(
									$("#messageListPanel").height()*2
							);

						}

					}, 0);

				}, function(e) {

					console.log(e);

				}
		);

	};

	$scope.changeUsername = function() {

		// do nothing

	};

	$scope.send = function() {

		// do nothing

	};

	$scope.sendPvt = function(username) {

		// do nothing

	};

});