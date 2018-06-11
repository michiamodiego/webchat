<html>
<head>
<title>WebChat</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link type="text/css" rel="stylesheet"
	href="./resources/css/bootstrap.min.css">
<link type="text/css" rel="stylesheet"
	href="./resources/css/webchat.css" />
</head>
<body>
	<div class="container height-fill" ng-app="chatApp"
		ng-controller="chatController">
		<div class="form-group row">
			<div class="col-md-8">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-8">
							<input type="text" class="form-control" name="username"
								placeholder="Your username here" ng-model="username"
								ng-disabled="!chatting">
						</div>
						<div class="col-md-4">
							<button type="button" class="btn btn-primary"
								ng-click="connect()" ng-show="!chatting">Connect</button>
							<button type="button" class="btn btn-primary"
								ng-click="changeUsername()" ng-show="chatting">Change username</button>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12" style="height: 70%">
							<div class="container-fluid pre-scrollable chat-message-panel"
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
						<div class="col-md-12">
							<input type="text" class="form-control" name="message"
								placeholder="Type something here" c-enter-key-down="send()"
								ng-model="message" ng-disabled="!chatting">
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-4">
				<div class="container-fluid bg-light rounded">
					<div class="row" ng-repeat="user in userList">
						<div class="col-md-12" ng-click="sendPvt(user)">{{user}}</div>
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