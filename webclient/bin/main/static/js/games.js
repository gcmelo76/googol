const url = 'https://free-to-play-games-database.p.rapidapi.com/api/filter?tag=3d.mmorpg.fantasy.pvp&platform=pc';
const options = {
  method: 'GET',
  headers: {
    'X-RapidAPI-Key': '26675a2eb4msh980b6e0d590bdadp1c8880jsnd15125c97e34',
    'X-RapidAPI-Host': 'free-to-play-games-database.p.rapidapi.com'
  }
};

function request() {
  try {
    const response = fetch(url, options).then((response) => {
      response.json().then((data) => { displayGames(data); })
    })
  } catch (error) {
    console.error(error);
  }
}

function displayGames(data){
  const parent = document.querySelector(".games");
  data.forEach((game) => {
    const card = document.createElement("div");
    card.classList.add("card");
    card.innerHTML = `
       <img src="${game.thumbnail}" alt="${game.title}-image" width=400 height=200 />
       <a class="button" href="${game.game_url}">${game.title}</a>
    `
    parent.appendChild(card);
    })
}

request();