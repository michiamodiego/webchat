<html>
<head>
<title>WebChat</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link type="text/css" rel="stylesheet"
	href="./resources/css/bootstrap.min.css">
<link type="text/css" rel="stylesheet"
	href="./resources/css/webchat.css" />
</head>
<body class="bg-white">
	<div class="container height-fill" ng-app="chatApp"
		ng-controller="chatController">
		<div class="form-group row bg-light rounded pb-2">
			<div class="col-md-8 pt-2">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-8 pt-2">
							<input type="text" class="form-control" name="username"
								placeholder="Your username here" ng-model="username"
								ng-disabled="!chatting">
						</div>
						<div class="col-md-4 pt-2">
							<button type="button" class="btn" ng-click="connect()"
								ng-show="!chatting">Connect</button>
							<button type="button" class="btn" ng-click="changeUsername()"
								ng-show="chatting">Change username</button>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12 pt-2">
							<div
								class="container-fluid pre-scrollable chat-message-panel rounded bg-white"
								id="messageListPanel">
								<div class="row" ng-show="!(messageList.length > 0)">
									<div class="col-md-12 text-center">No message to show.</div>
								</div>
								<div class="row" ng-repeat="message in messageList">
									<div class="col-md-12">{{message.message}}</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-8 pt-2">
							<input type="text" class="form-control" name="message"
								placeholder="Type something here" c-enter-key-down="send()"
								ng-model="message" ng-disabled="!chatting">
						</div>
						<div class="col-md-4 pt-2">
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox"
									id="autoScrollBar" value="t" ng-model="scrolling"
									ng-disabled="!chatting"> <label
									class="form-check-label" for="autoScrollBar">Auto
									ScrollBar</label>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-4 pt-2">
				<div class="container-fluid bg-light rounded bg-white">
					<div class="row" ng-repeat="user in userList">
						<div class="col-md-12 webchat-cursor" ng-click="sendPvt(user)">{{user}}</div>
					</div>
					<div class="row" ng-show="!(userList.length > 0)">
						<div class="col-md-12 text-center">No user to show.</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="./resources/js/jsextensions.js"></script>
	<script type="text/javascript" src="./resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="./resources/js/angular.min.js"></script>
	<script type="text/javascript" src="./resources/js/chatmodel.js"></script>
	<script type="text/javascript" src="./resources/js/chatapp.js"></script>
</body>
</html>