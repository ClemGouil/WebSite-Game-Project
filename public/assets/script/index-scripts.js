// index-scripts.js
document.addEventListener('DOMContentLoaded', function() {
  showPage('home'); // Affiche la page d'accueil par défaut
  checkLoginStatus(); // Vérifie l'état de connexion de l'utilisateur
  fetchQuestions()

  const notifImage = document.getElementById('notif');

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

  var isLoggedIn = localStorage.getItem('isLogged') === 'true';
  console.log('isLoggedIn:', isLoggedIn);

  var loginButton = document.getElementById('login-button');
  var profile = document.getElementById('profile');
  var notif = document.getElementById('notif');
  var profileMenu = document.getElementById('profile-menu');

  if (isLoggedIn) {
    loginButton.style.display = 'none';
    profile.style.display = 'flex';
    notif.style.display = 'flex';
    profileMenu.style.display = 'none';
  } else {
    loginButton.style.display = 'flex';
    profile.style.display = 'none';
    notif.style.display = 'none';
  }
}

function updateProfileInfo() {
  
  // Récupérer les valeurs du Local Storage
  var username = localStorage.getItem('username');
  var email = localStorage.getItem('email');

  console.log('Updated Info',username, email);
  // Mettre à jour le menu du profil
  document.getElementById('profile-username-menu').textContent = username || 'Nom non défini';
  document.getElementById('profile-email-menu').textContent = email || 'Email non défini';
}

function toggleProfileMenu() {
  var profileMenu = document.getElementById('profile-menu');
  if (profileMenu.style.display === 'block') {
    profileMenu.style.display = 'none';
  } else {
    updateProfileInfo()
    profileMenu.style.display = 'block';
  }
}

function editProfile() {

}

function logout() {
  // Logic to logout the user
  localStorage.removeItem('username');
  localStorage.removeItem('email');
  localStorage.setItem('isLogged',false);
  window.location.href = 'index.html';
}

async function fetchQuestions() {
  try {
      var apiEndpoint = 'http://localhost/GameApp/questions'; 
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
              <p>${truncateText(question.content, 100)}</p>
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

document.getElementById('submitQuestionForm').addEventListener('submit', function(event) {
  event.preventDefault(); // Empêche le comportement par défaut du formulaire

  // Récupérer les valeurs du formulaire
  var username = localStorage.getItem('username')
  var title = document.getElementById('title').value;
  var content = document.getElementById('content').value;

  // Construire l'objet utilisateur
  var questionData = {
    "title": title,
    "id_user": 10,
    "date": "",
    "content": content
  };

  // Options de la requête fetch
  var requestOptions = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(questionData)
  };

  // URL de mon API
  var apiEndpoint = 'http://localhost/GameApp/questions/create'; 

  // Effectuer la requête fetch
  fetch(apiEndpoint, requestOptions)
    .then(function(response) {
      if (!response.ok) {
        throw new Error('Erreur HTTP ' + response.status);
      }
      return response.json();
    })
    .then(function(data) {
      console.log('Réponse de l\'API :', data);

      // Traitez la réponse de l'API ici
      alert('Question Ajouté !');
      document.getElementById('submitQuestionForm').reset();
      window.location.reload();
    })
    .catch(function(error) {
      console.error('Erreur lors de la requête :', error);
      // Gérez les erreurs ici, par exemple affichage d'un message d'erreur à l'utilisateur
      alert('Erreur lors de l\'ajout de la question : ' + error.message); // Exemple d'une alerte d'erreur
    });
});