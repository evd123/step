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

google.charts.load('current', {
  'packages': ['corechart']
});
google.charts.setOnLoadCallback(drawChart);

/** Creates a chart and adds it to the page. */
function drawChart() {
  fetch('/travel-data').then(response => response.json())
    .then((voteCount) => {
      const data = new google.visualization.DataTable();
      data.addColumn('string', 'Continent');
      data.addColumn('number', 'Votes');
      Object.keys(voteCount).forEach((continent) => {
        data.addRow([continent, voteCount[continent]]);
      });

      const options = {
        'width': 500,
        'height': 400,
        'padding': 0
      };

      const chart = new google.visualization.PieChart(
        document.getElementById('chart-container'));
      chart.draw(data, options);
    });
}

function getComments() {
  fetch('/data').then(response => response.json()).then((messages) => {
    const historyEl = document.getElementById('history');
    messages.forEach((message) => {
      historyEl.appendChild(createMessageElement(message));
    });
  });
}

/** Creates an element that represents a task, including its delete button. */
function createMessageElement(message) {
  const messageElement = document.createElement('li');
  messageElement.className = 'message';

  const titleElement = document.createElement('span');
  titleElement.innerText = message.comment;

  const deleteButtonElement = document.createElement('button1');
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
    document.getElementById('map'), {
      center: {
        lat: 39.8283,
        lng: -99.5
      },
      zoom: 5.5
    });

  // Travel/NSLI-Y marker
  var travelString = '<div id="content">' +
    '<div id="siteNotice">' +
    '</div>' +
    '<h1 id="firstHeading" class="firstHeading">Nanjing, China</h1>' +
    '<div id="bodyContent">' +
    '<p>I love to travel, and Nanjing, China has been a favorite of the places I have visited! ' +
    'Through the National Security Language Initiative for Youth Scholarship, ' +
    'I lived with a host family and attended language and culture classes at Nanjing University for 6 weeks. ' +
    'A past capital of China, Nanjing has incredible historical sights and amazing food!</p>' +
    '<center>' +
    '<a href="images/travel.png"><img src="images/travel.png" width=230 align="middle"/></a>' +
    '</center' +
    '</br>' +
    '</div>' +
    '</div>';

  var travelWindow = new google.maps.InfoWindow({
    content: travelString,
  });

  var travelMarker = new google.maps.Marker({
    position: {
      lat: 32.0603,
      lng: 118.7969
    },
    zoom: 9,
    map: map,
    title: 'Nanjing, China'
  });
  travelMarker.addListener('click', function() {
    travelWindow.open(map, travelMarker);
  });

  // Skiing marker
  var skiString = '<div id="content">' +
    '<div id="siteNotice">' +
    '</div>' +
    '<h1 id="firstHeading" class="firstHeading">Empire Bowl</h1>' +
    '<div id="bodyContent">' +
    '<p>Winter is my favorite season because: skiing!</p> ' +
    '<center>' +
    '<a href="images/skiing.png"><img src="images/skiing.png" width=230 align="middle"/></a>' +
    '</center>' +
    '</br>' +
    '</div>' +
    '</div>';

  var skiWindow = new google.maps.InfoWindow({
    content: skiString
  });

  var skiMarker = new google.maps.Marker({
    position: {
      lat: 40.6546,
      lng: -111.49717
    },
    zoom: 9,
    map: map,
    title: 'Empire Bowl'
  });
  skiMarker.addListener('click', function() {
    skiWindow.open(map, skiMarker);
  });

  // Books marker
  var bookString = '<div id="content">' +
    '<div id="siteNotice">' +
    '</div>' +
    '<h1 id="firstHeading" class="firstHeading">The Book Loft</h1>' +
    '<div id="bodyContent">' +
    '<p>This is my favorite book store; it used to be a house, but now every room holds a different genre.</p> ' +
    '<center>' +
    '<a href="images/books.png"><img src="images/books.png" width=230 align="middle"/></a>' +
    '</center>' +
    '</br>' +
    '</div>' +
    '</div>';

  var bookWindow = new google.maps.InfoWindow({
    content: bookString
  });

  var bookMarker = new google.maps.Marker({
    position: {
      lat: 39.94949,
      lng: -82.995613
    },
    zoom: 9,
    map: map,
    title: 'Book Loft'
  });
  bookMarker.addListener('click', function() {
    bookWindow.open(map, bookMarker);
  });

  // Coffee marker
  var coffeeString = '<div id="content">' +
    '<div id="siteNotice">' +
    '</div>' +
    '<h1 id="firstHeading" class="firstHeading">Rice Coffeehouse</h1>' +
    '<div id="bodyContent">' +
    '<p>"Chaus" is one of my favorite spots on campus! ' +
    '<center>' +
    '<a href="images/coffee.png"><img src="images/coffee.png" width=230 align="middle"/></a>' +
    '</center>' +
    '</br>' +
    '</div>' +
    '</div>';

  var coffeeWindow = new google.maps.InfoWindow({
    content: coffeeString
  });

  var coffeeMarker = new google.maps.Marker({
    position: {
      lat: 29.71661,
      lng: -95.402618
    },
    zoom: 9,
    map: map,
    title: 'Chaus'
  });
  coffeeMarker.addListener('click', function() {
    coffeeWindow.open(map, coffeeMarker);
  });

  // Home marker
  var homeString = '<div id="content">' +
    '<div id="siteNotice">' +
    '</div>' +
    '<h1 id="firstHeading" class="firstHeading">New Albany, OH</h1>' +
    '<div id="bodyContent">' +
    '<p>My hometown, featuring my dog, Oscar!</p> ' +
    '<center>' +
    '<a href="images/dog.jpg"><img src="images/dog.jpg" width=230 align="middle"/></a>' +
    '</center>' +
    '</br>' +
    '</div>' +
    '</div>';

  var homeWindow = new google.maps.InfoWindow({
    content: homeString
  });

  var homeMarker = new google.maps.Marker({
    position: {
      lat: 40.0813,
      lng: -82.8087
    },
    zoom: 9,
    map: map,
    title: 'Home'
  });
  homeMarker.addListener('click', function() {
    homeWindow.open(map, homeMarker);
  });

  // Google marker
  var googleString = '<div id="content">' +
    '<div id="siteNotice">' +
    '</div>' +
    '<h1 id="firstHeading" class="firstHeading">Google</h1>' +
    '<div id="bodyContent">' +
    "<p>This summer, I am interning with Google as a Student Training in Engineering Program intern on the Chronicle team! " +
    "I completed group and individual projects (including this website) and gained more experience with " +
    "HTML, CSS, JavaScript, Java, and Google's APIs.</p>" +
    '</br>' +
    '</div>' +
    '</div>';

  var googleWindow = new google.maps.InfoWindow({
    content: googleString
  });

  var googleMarker = new google.maps.Marker({
    position: {
      lat: 40.798939,
      lng: -73.024742
    },
    zoom: 9,
    map: map,
    title: 'Google'
  });
  googleMarker.addListener('click', function() {
    googleWindow.open(map, googleMarker);
  });

  // Pinterest marker
  var pinString = '<div id="content">' +
    '<div id="siteNotice">' +
    '</div>' +
    '<h1 id="firstHeading" class="firstHeading">Pinterest</h1>' +
    '<div id="bodyContent">' +
    "<p>I am an Engage Scholar with Pinterest this summer! The Engage Scholar Program is a " +
    "technical training program designed to help students connect with industry professionals " +
    "and improve technical and interpersonal skills for future success in software engineering</p>" +
    '</br>' +
    '</div>' +
    '</div>';

  var pinWindow = new google.maps.InfoWindow({
    content: pinString
  });

  var pinMarker = new google.maps.Marker({
    position: {
      lat: 37.775246,
      lng: -122.399734
    },
    zoom: 9,
    map: map,
    title: 'Pinterest'
  });
  pinMarker.addListener('click', function() {
    pinWindow.open(map, pinMarker);
  });

  // Capital One marker
  var cap1String = '<div id="content">' +
    '<div id="siteNotice">' +
    '</div>' +
    '<h1 id="firstHeading" class="firstHeading">Capital One</h1>' +
    '<div id="bodyContent">' +
    "<p>I participated in Capital One's Summit for Developing Leaders in 2019! " +
    "This was a sophomore program hosted at Capital One's Texas headquarters for prospective " +
    "Business Analysts.</p>" +
    '</br>' +
    '</div>' +
    '</div>';

  var cap1Window = new google.maps.InfoWindow({
    content: cap1String
  });

  var cap1Marker = new google.maps.Marker({
    position: {
      lat: 33.082160,
      lng: -96.803208
    },
    zoom: 9,
    map: map,
    title: 'Cap1'
  });
  cap1Marker.addListener('click', function() {
    cap1Window.open(map, cap1Marker);
  });

  // BridgeYear marker
  var byString = '<div id="content">' +
    '<div id="siteNotice">' +
    '</div>' +
    '<h1 id="firstHeading" class="firstHeading">BridgeYear</h1>' +
    '<div id="bodyContent">' +
    "<p>Last summer (2019), I interned with BridgeYear as a Data Analysis Intern. " +
    "I cleaned and analyzed data from surveys and student-mentor text messages with Python and R, " +
    "designed and presented solutions and feedback on data trends, "
  "and translated data and graphs into distributable formats and promotional materials.</p>" +
  '</br>' +
  '</div>' +
  '</div>';

  var byWindow = new google.maps.InfoWindow({
    content: byString
  });

  var byMarker = new google.maps.Marker({
    position: {
      lat: 29.717210,
      lng: -95.471665
    },
    zoom: 9,
    map: map,
    title: 'BY'
  });
  byMarker.addListener('click', function() {
    byWindow.open(map, byMarker);
  });

  // HackRice marker
  var hrString = '<div id="content">' +
    '<div id="siteNotice">' +
    '</div>' +
    '<h1 id="firstHeading" class="firstHeading">HackRice</h1>' +
    '<div id="bodyContent">' +
    "<p>This year, I am co-directing HackRice X, the 10th anniversary of Rice University's annual " +
    "hackathon that reaches over 400 students from around the world. I organize and oversee the " +
    "HackRice Board, a team of 12 undergraduate students, network with potential corporate partners " +
    "and manage a $40,000+ budget.</p>" +
    '</br>' +
    '</div>' +
    '</div>';

  var hrWindow = new google.maps.InfoWindow({
    content: hrString
  });

  var hrMarker = new google.maps.Marker({
    position: {
      lat: 29.719023,
      lng: -95.397827
    },
    zoom: 9,
    map: map,
    title: 'HR'
  });
  hrMarker.addListener('click', function() {
    hrWindow.open(map, hrMarker);
  });

  // WIA marker
  var wiaString = '<div id="content">' +
    '<div id="siteNotice">' +
    '</div>' +
    '<h1 id="firstHeading" class="firstHeading">Women in Analytics</h1>' +
    '<div id="bodyContent">' +
    "<p>This year, I am a student ambassador with the Women in Analytics conference. I help to inform " +
    "potential student attendees of the exciting opportunities (scholarships, competitions, hackathons, " +
    "speaker series, the conference itself) available to them, strategize funding for sutdent attendees " +
    "and logistics related to summer internships, and provide input on enhancing the WIA student experience.</p>" +
    '</br>' +
    '</div>' +
    '</div>';

  var wiaWindow = new google.maps.InfoWindow({
    content: wiaString
  });

  var wiaMarker = new google.maps.Marker({
    position: {
      lat: 39.9612,
      lng: -82.9988
    },
    zoom: 9,
    map: map,
    title: 'WIA'
  });
  wiaMarker.addListener('click', function() {
    wiaWindow.open(map, wiaMarker);
  });

  // WiCS marker
  var wicsString = '<div id="content">' +
    '<div id="siteNotice">' +
    '</div>' +
    '<h1 id="firstHeading" class="firstHeading">WiCS Hack</h1>' +
    '<div id="bodyContent">' +
    "<p>My team placed 3rd overall in the UT Austin Women in Computer Science Hackathon! " +
    "We utilized JavaScript, React Native, Spotify's API, and CSS to develop a Mood-a-Day visual calendar " +
    "that added a song based on the user's inputted mood to a designated playlist in their Spotify account.</p>" +
    '</br>' +
    '</div>' +
    '</div>';

  var wicsWindow = new google.maps.InfoWindow({
    content: wicsString
  });

  var wicsMarker = new google.maps.Marker({
    position: {
      lat: 30.284381,
      lng: -97.734725
    },
    zoom: 9,
    map: map,
    title: 'WiCS'
  });
  wicsMarker.addListener('click', function() {
    wicsWindow.open(map, wicsMarker);
  });

  // Rice marker
  var riceString = '<div id="content">' +
    '<div id="siteNotice">' +
    '</div>' +
    '<h1 id="firstHeading" class="firstHeading">Rice University</h1>' +
    '<div id="bodyContent">' +
    "<p>I am a junior at Rice University studying Computer Science and Statistics. " +
    "I am interested in exploring opportunities in data analytics, software engineering, project management, and business consulting.</p>" +
    '</br>' +
    '</div>' +
    '</div>';

  var riceWindow = new google.maps.InfoWindow({
    content: riceString
  });

  var riceMarker = new google.maps.Marker({
    position: {
      lat: 29.720126,
      lng: -95.398847
    },
    zoom: 9,
    map: map,
    title: 'Rice'
  });
  riceMarker.addListener('click', function() {
    riceWindow.open(map, riceMarker);
  });
}
