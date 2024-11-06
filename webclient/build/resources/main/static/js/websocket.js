var stompClient = null;

function connect() {
    var socket = new SockJS('/my-websocket');
    stompClient = Stomp.over(socket);
    console.log(stompClient);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/messages', function (message) {
            const result = JSON.parse(JSON.parse(message.body).content);
            console.log(result);
            displaySearches(result.topSearches);
            displayBarrels(result.barrelMetrics);
        });
    });
}

connect();

function displaySearches(items){
    const topSearchesElement = document.querySelector('#searches');
    topSearchesElement.innerHTML = '';
    items.forEach(item => {
       const topItem = document.createElement('li');
       topItem.textContent = item;
       topSearchesElement.appendChild(topItem);
    });
}

function displayBarrels(items){
    const barrels = document.querySelector('#barrels');
    barrels.innerHTML = `
        <tr>
            <th>Barrel</th>
            <th>Time/ds</th>
        </tr>
    `;
    items.forEach(item => {
           const topItem = document.createElement('tr');
              topItem.innerHTML = `
                 <td>${item.barrelId}</td>
                 <td>${item.average}</td>
              `;
           barrels.appendChild(topItem);
    });
}