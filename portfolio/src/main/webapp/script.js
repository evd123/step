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
