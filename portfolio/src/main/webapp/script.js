// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
function initialize() {
    getComments();
    getMap();
}

function getComments() {
	fetch('/data').then(response => response.json()).then((messages) => {
		const historyEl = document.getElementById('history');
		messages.forEach((message) => {
			historyEl.appendChild(createMessageElement(message));
		});
	});
}

function createListElement(text) {
	const liElement = document.createElement('li');
	liElement.innerText = text;
	return liElement;
}

/** Creates an element that represents a task, including its delete button. */
function createMessageElement(message) {
	const messageElement = document.createElement('li');
	messageElement.className = 'message';

	const titleElement = document.createElement('span');
	titleElement.innerText = message.comment;

	const deleteButtonElement = document.createElement('button');
	deleteButtonElement.innerText = 'Delete';
	deleteButtonElement.addEventListener('click', () => {
		deleteMessage(message);

		// Remove the task from the DOM.
		messageElement.remove();
	});

	messageElement.appendChild(titleElement);
	messageElement.appendChild(deleteButtonElement);
	return messageElement;
}

/** Tells the server to delete the task. */
function deleteMessage(message) {
	const params = new URLSearchParams();
	params.append('id', message.id);
	fetch('/delete-message', {
		method: 'POST',
		body: params
	});
}

function getMap() {
    const map = new google.maps.Map(
        document.getElementById('map'), 
        {center: {lat: 39.8283, lng: -98.5795}, zoom: 5});
    
    // Travel/NSLI-Y marker
    var travelString = '<div id="content">'+
      '<div id="siteNotice">'+
      '</div>'+
      '<h1 id="firstHeading" class="firstHeading">Nanjing, China</h1>'+
      '<div id="bodyContent">'+
      '<p>I love to travel, and Nanjing, China has been a favorite of the places I have visited! ' +
      'Through the <a href="https://www.nsliforyouth.org/" target = "_blank">National Security Language Initiative for Youth Scholarship</a>, '+
      'I lived with a host family and attended language and culture classes at Nanjing University for 6 weeks. '+
      'A past capital of China, Nanjing has incredible historical sights and amazing food!</p>'+
      '<p class="aligncenter">' +
        '<a href="images/travel.png"><img src="images/travel.png" width=230/></a>' +
      '</p>'+
      '</div>'+
      '</div>';

    var travelWindow = new google.maps.InfoWindow({
      content: travelString
    });

    var travelMarker = new google.maps.Marker({
      position: {lat: 32.0603, lng: 118.7969},
      zoom: 9, 
      map: map,
      title: 'Nanjing, China'
    });
    travelMarker.addListener('click', function() {
      travelWindow.open(map, travelMarker);
    });

    // Skiing marker
    var skiString = '<div id="content">'+
      '<div id="siteNotice">'+
      '</div>'+
      '<h1 id="firstHeading" class="firstHeading">Empire Bowl</h1>'+
      '<div id="bodyContent">'+
      '<p>I love getting outside and being active and skiing is the best adrenaline rush ' +
      '<p class="aligncenter">' +
        '<a href="images/skiing.png"><img src="images/skiing.png" width=230/></a>' +
      '</p>'+
      '</div>'+
      '</div>';

    var skiWindow = new google.maps.InfoWindow({
      content: skiString
    });

    var skiMarker = new google.maps.Marker({
      position: {lat: 40.6546, lng: -111.49717},
      zoom: 9, 
      map: map,
      title: 'Empire Bowl'
    });
    skiMarker.addListener('click', function() {
      skiWindow.open(map, skiMarker);
    });

    // Books marker
    var bookString = '<div id="content">'+
      '<div id="siteNotice">'+
      '</div>'+
      '<h1 id="firstHeading" class="firstHeading">The Book Loft</h1>'+
      '<div id="bodyContent">'+
      '<p>This is my favorite book store; it used to be a house, but now every room holds a different genre. ' +
      '<p class="aligncenter">' +
        '<a href="images/books.png"><img src="images/books.png" width=230/></a>' +
      '</p>'+
      '</div>'+
      '</div>';

    var bookWindow = new google.maps.InfoWindow({
      content: bookString
    });

    var bookMarker = new google.maps.Marker({
      position: {lat: 39.94949, lng: -82.995613},
      zoom: 9, 
      map: map,
      title: 'Book Loft'
    });
    bookMarker.addListener('click', function() {
      bookWindow.open(map, bookMarker);
    });

    // Coffee marker
    var coffeeString = '<div id="content">'+
      '<div id="siteNotice">'+
      '</div>'+
      '<h1 id="firstHeading" class="firstHeading">Rice Coffeehouse</h1>'+
      '<div id="bodyContent">'+
      '<p>As my coffee addiction knows no end, "Chaus" is a favorite campus spot of mine. ' +
      'Their oatmilk lattes are to-die-for. ' +
      '<p class="aligncenter">' +
        '<a href="images/coffee.png"><img src="images/coffee.png" width=230/></a>' +
      '</p>'+
      '</div>'+
      '</div>';

    var coffeeWindow = new google.maps.InfoWindow({
      content: coffeeString
    });

    var coffeeMarker = new google.maps.Marker({
      position: {lat: 29.71661, lng: -95.402618},
      zoom: 9, 
      map: map,
      title: 'Chaus'
    });
    coffeeMarker.addListener('click', function() {
      coffeeWindow.open(map, coffeeMarker);
    });

    // Home marker
    var homeString = '<div id="content">'+
      '<div id="siteNotice">'+
      '</div>'+
      '<h1 id="firstHeading" class="firstHeading">New Albany, OH</h1>'+
      '<div id="bodyContent">'+
      '<p>My hometown, featuring my dog, Oscar! ' +
      '<p class="aligncenter">' +
        '<a href="images/dog.jpg"><img src="images/dog.jpg" width=230/></a>' +
      '</p>'+
      '</div>'+
      '</div>';

    var homeWindow = new google.maps.InfoWindow({
      content: homeString
    });

    var homeMarker = new google.maps.Marker({
      position: {lat: 40.0813, lng: -82.8087},
      zoom: 9, 
      map: map,
      title: 'Home'
    });
    homeMarker.addListener('click', function() {
      homeWindow.open(map, homeMarker);
    });
}