// index-scripts.js
document.addEventListener('DOMContentLoaded', function() {
  showPage('home'); // Affiche la page d'accueil par défaut
  checkLoginStatus(); // Vérifie l'état de connexion de l'utilisateur
  fetchQuestions();

  const profileImage = document.getElementById('profile');
  const notifImage = document.getElementById('notif');

  profileImage.addEventListener('click', function() {
    showPage("profile-info");
    updateProfileInfo();
  });

  notifImage.addEventListener('click', function() {

  });
});

function showPage(pageId) {
  const pages = document.querySelectorAll('.page');
  pages.forEach(page => {
    page.classList.remove('active');
  });
  document.getElementById(pageId).classList.add('active');
}

function checkLoginStatus() {

  var isLoggedIn = False;
  //var isLoggedIn = localStorage.getItem('isLogged') === 'true';

  var loginButton = document.getElementById('login-button');
  var profile = document.getElementById('profile');
  var notif = document.getElementById('notif');

  if (isLoggedIn) {
    loginButton.style.display = 'none';
    profile.style.display = 'flex';
    notif.style.display = 'flex';
  } else {
    loginButton.style.display = 'flex';
    profile.style.display = 'none';
    notif.style.display = 'none';
  }
}

async function fetchQuestions() {
  try {
      var apiEndpoint = 'http://localhost/GameApp/questions/create'; 
      const response = await fetch(apiEndpoint);
      const questions = await response.json();
      displayQuestions(questions);
  } catch (error) {
      console.error('Erreur lors de la récupération des questions:', error);
  }
}

function displayQuestions(questions) {
  const container = document.getElementById('questions-container');
  questions.forEach(question => {
      const card = document.createElement('div');
      card.className = 'question-card';

      card.innerHTML = `
          <img src="assets/ressource/Profile.jpg" alt="Photo de profil" class="profile-pic">
          <div class="question-content">
              <div class="username">${question.id_user}</div>
              <h3>${question.title}</h3>
              <p>${truncateText(question.description, 100)}</p>
          </div>
      `;
      container.appendChild(card);
  });
}

function truncateText(text, maxLength) {
  if (text.length > maxLength) {
      return text.substring(0, maxLength) + '...';
  }
  return text;
}

function updateProfileInfo() {
  // Récupérer les valeurs du Local Storage
  var username = localStorage.getItem('username');
  var email = localStorage.getItem('email');

  // Mettre à jour les éléments du DOM avec les valeurs récupérées
  document.getElementById('profile-username').textContent = username || 'Nom non défini';
  document.getElementById('profile-email').textContent = email || 'Email non défini';
}

