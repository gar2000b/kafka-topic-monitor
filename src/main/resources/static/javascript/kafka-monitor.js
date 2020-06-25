/*
 * Name: kafka-monitor.js
 * Author: Gary Black - 330885096
 * Contact: gar2000b@yahoo.com
 */

var latestPayload = "";
var topics = [];
var tableConstructed = false;
var timeouts = [];
var sessionTimeout;

var stompClient = null;
var uuid = generateUUID();
connect();

window.onload = function() {
	getToken();
	getSessionTimeout();
}

function connect() {
	var socket = new SockJS('/kafka-topic-monitor-websocket');
	stompClient = Stomp.over(socket);
	stompClient.debug = null;
	stompClient.connect({}, function(frame) {
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/record/' + uuid, function(payload) {
			if (payload.body.length > 0) {
				document.getElementById("payload").value = payload.body;
			}
		});
		stompClient.subscribe('/topic/topicDetails', function(response) {
			topics = JSON.parse(response.body);
			if (tableConstructed == false) {
				constructTable(topics);
				tableConstructed = true;
			} else {
				updateTable(topics);
			}
		});
	});
}

function fetchRecord() {
	var topicName = document.getElementById("topicname").value;
	var offset = document.getElementById("offset").value;
	stompClient.send("/app/fetchRecord/" + uuid, {}, JSON.stringify({
		'topicName' : topicName,
		'offset' : offset
	}));
}

function back() {
	var offset = parseInt(document.getElementById("offset").value, 10);
	offset -= 1;
	document.getElementById("offset").value = offset;
	fetchRecord();
}

function forward() {
	var offset = parseInt(document.getElementById("offset").value, 10);
	offset += 1;
	document.getElementById("offset").value = offset;
	fetchRecord();
}

function getTopics() {
	stompClient.send("/app/fetchTopicDetails", {}, "");
}

function disconnect() {
	if (stompClient !== null) {
		stompClient.disconnect();
	}
	setConnected(false);
	console.log("Disconnected");
}

function updateTable(topics) {
	// console.log('** updateTable **')
	for (var i = 0; i < topics.length; i++) {
		var name = topics[i].name;
		var consumerGroup = topics[i].consumerGroup;
		var environment = topics[i].environment;
		var offset = topics[i].offset;
		var payload = topics[i].payload;
		var latestPayloadDateTime = topics[i].latestPayloadDateTime;
		latestPayload = payload;

		var topicTable = document.getElementById("topicTable");
		if (topicTable.rows[i + 1].cells[4].innerHTML != offset) {
			var topicImg = document.getElementById("topicImg" + i);
			topicImg.src = "images/incoming-topic.png";
			topicTable.rows[i + 1].cells[4].innerHTML = offset;
			topicTable.rows[i + 1].cells[4].style.color = "#00AA00";
			var cellToUpdate = topicTable.rows[i + 1].cells[4];
			clearTimeout(timeouts[i + 1]);
			timeouts[i + 1] = setTimeout(resetOffset, 3000, cellToUpdate);
			topicTable.rows[i + 1].cells[5].innerHTML = "<a onclick=\"window.scrollTo(0,document.body.scrollHeight); renderLatestPayload('"
					+ i
					+ "'); return false;\" href=\"#\" title=\"Click here to view last payload\">"
					+ latestPayloadDateTime + "</a>";
		}
	}
}

function resetOffset(cellToUpdate) {
	//console.log("before: " + cellToUpdate.style.color);
	cellToUpdate.style.color = "#FFFFFF";
	//console.log("after: " + cellToUpdate.style.color);
	resetImage();
}

function constructTable(topics) {
	var topicTable = document.getElementById("topicTable");
	topicTable.innerHTML = "";
	var headerRow = topicTable.insertRow(0);
	var cellHeader0 = headerRow.insertCell(0);
	var cellHeader1 = headerRow.insertCell(1);
	var cellHeader2 = headerRow.insertCell(2);
	var cellHeader3 = headerRow.insertCell(3);
	var cellHeader4 = headerRow.insertCell(4);
	var cellHeader5 = headerRow.insertCell(5);

	cellHeader0.innerHTML = "Topic";
	cellHeader0.style.textAlign = "center";
	cellHeader1.innerHTML = "Name";
	cellHeader2.innerHTML = "Consumer Group";
	cellHeader3.innerHTML = "Environment";
	cellHeader4.innerHTML = "Offset";
	cellHeader5.innerHTML = "Latest Payload";

	for (var i = 0; i < topics.length; i++) {
		var name = topics[i].name;
		var consumerGroup = topics[i].consumerGroup;
		var environment = topics[i].environment;
		var offset = topics[i].offset;
		var payload = topics[i].payload;
		var latestPayloadDateTime = topics[i].latestPayloadDateTime;
		latestPayload = name;

		var rowCount = document.getElementById("topicTable").rows.length;
		var row = topicTable.insertRow(rowCount);
		var cell0 = row.insertCell(0);
		var cell1 = row.insertCell(1);
		var cell2 = row.insertCell(2);
		var cell3 = row.insertCell(3);
		var cell4 = row.insertCell(4);
		var cell5 = row.insertCell(5);

		cell0.innerHTML = "<img id=\"topicImg"
				+ i
				+ "\" alt=\"ABC\" src=\"images/regular-topic.png\" style=\"width: 136px; height: 36px; \"/>";
		cell1.innerHTML = name;
		cell2.innerHTML = consumerGroup;
		cell3.innerHTML = environment;
		cell4.innerHTML = offset;
		cell5.innerHTML = "<a onclick=\"window.scrollTo(0,document.body.scrollHeight); renderLatestPayload('"
				+ i
				+ "'); return false;\" href=\"#\" title=\"Click here to view last payload\">"
				+ latestPayloadDateTime + "</a>";
	}
}

function resetImage() {
	for (var i = 0; i < topics.length; i++) {
		var topicImg = document.getElementById("topicImg" + i);
		if (topicImg.src.indexOf("regular-topic.png") == -1) {
			topicImg.src = "images/regular-topic.png";
		}
	}
}

function renderLatestPayload(i) {
	if (topics[i].payload != '') {
		document.getElementById("topicname").value = topics[i].name;
		document.getElementById("consumergroup").value = topics[i].consumerGroup;
		document.getElementById("offset").value = topics[i].offset;
		document.getElementById("payload").value = topics[i].payload;
	} else {
		document.getElementById("topicname").value = topics[i].name;
		document.getElementById("consumergroup").value = topics[i].consumerGroup;
		document.getElementById("offset").value = topics[i].offset;
		document.getElementById("payload").value = 'No Payload received since app start';
	}
}

function generateUUID() {
	var d = new Date().getTime();
	if (typeof performance !== 'undefined'
			&& typeof performance.now === 'function') {
		d += performance.now(); // use high-precision timer if available
	}
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		var r = (d + Math.random() * 16) % 16 | 0;
		d = Math.floor(d / 16);
		return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
	});
}

function getToken() {
	var thisdate = new Date();
	var serverPage = "token";

	var xmlhttp1 = getxmlhttp();
	xmlhttp1.open("GET", serverPage);
	xmlhttp1.onreadystatechange = function() {
		getTokenAjaxHandler(xmlhttp1);
	}
	xmlhttp1.send(null);
}

function getTokenAjaxHandler(xmlhttp1) {
	if (xmlhttp1.readyState == 4 && xmlhttp1.status == 200) {
		var response = xmlhttp1.responseText;
		token = response;
		document.getElementById('_csrf_logout').value = token;
	}
}

function getSessionTimeout() {
	var thisdate = new Date();
	var serverPage = "session-timeout";

	var xmlhttp1 = getxmlhttp();
	xmlhttp1.open("GET", serverPage);
	xmlhttp1.onreadystatechange = function() {
		getSessionTimeoutAjaxHandler(xmlhttp1);
	}
	xmlhttp1.send(null);
}

function getSessionTimeoutAjaxHandler(xmlhttp1) {
	if (xmlhttp1.readyState == 4 && xmlhttp1.status == 200) {
		var response = xmlhttp1.responseText;
		sessionTimeout = response;
	}
}

function logout() {
	document.getElementById('logout').submit();
}

function resetConsumers() {
	var thisdate = new Date();
	var serverPage = "reset-consumers";

	var xmlhttp1 = getxmlhttp();
	xmlhttp1.open("POST", serverPage);
	xmlhttp1.onreadystatechange = function() {
		resetConsumersAjaxHandler(xmlhttp1);
	}
	xmlhttp1.setRequestHeader("x-csrf-token", token);
	xmlhttp1.send(null);
}

function resetConsumersAjaxHandler(xmlhttp1) {
	if (xmlhttp1.readyState == 4 && xmlhttp1.status == 200) {
		var response = xmlhttp1.responseText;
		console.log("Kafka Consumers Resetting");
	}
}