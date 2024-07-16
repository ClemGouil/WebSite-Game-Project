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
  showPage("update-profile")
  toggleProfileMenu()
}

function logout() {
  // Logic to logout the user
  localStorage.removeItem('username');
  localStorage.removeItem('email');
  localStorage.setItem('isLogged',false);
  window.location.href = 'index.html';
  toggleProfileMenu()
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

async function getUserById(id) {
  try {
      var apiEndpoint = 'http://localhost/GameApp/users/' + id; 
      const response = await fetch(apiEndpoint);
      if (response.ok) {
          return await response.json();
      } else {
          throw new Error('User not found');
      }
  } catch (error) {
      console.error('Erreur lors de la récupération user', error);
      throw error; // Propager l'erreur pour qu'elle soit capturée par l'appelant
  }
}

async function displayQuestions(questions) {
  const container = document.getElementById('questions-container');
  for (const question of questions) {
      try {
          const user = await getUserById(question.id_user);
          const username = user.username;
          createQuestionCard(container, question, username);
      } catch (error) {
          console.error(`Error fetching user with ID ${question.id_user}:`, error);
          const username = 'Utilisateur inconnu'; // Valeur par défaut
          createQuestionCard(container, question, username);
      }
  }
}

function createQuestionCard(container, question, username) {
  const card = document.createElement('div');
  card.className = 'question-card';

  card.innerHTML = `
      <img src="assets/ressource/Profile.jpg" alt="Photo de profil" class="profile-pic">
      <div class="question-content">
          <div class="username">${username}</div>
          <h3>${question.title}</h3>
          <p>${truncateText(question.content, 100)}</p>
      </div>
  `;
  container.appendChild(card);
}

function truncateText(text, maxLength) {
  if (text.length > maxLength) {
      return text.substring(0, maxLength) + '...';
  }
  return text;
}

document.getElementById('submitQuestionForm').addEventListener('submit', function(event) {
  event.preventDefault(); // Empêche le comportement par défaut du formulaire

  // Vérifier si l'utilisateur est connecté
  var isLogged = localStorage.getItem('isLogged');
  if (isLogged !== 'true') {
      alert('Veuillez vous connecter pour soumettre une question.');
      window.location.href = 'login.html'; // Rediriger vers la page de connexion
      return;
  }

  // Récupérer l'ID de l'utilisateur depuis le localStorage et convertir en entier
  var userIdString = localStorage.getItem('userId');
  var userId = parseInt(userIdString, 10);

  // Vérifier si userId est un nombre valide
  if (isNaN(userId)) {
      console.error('Erreur : userId n\'est pas un nombre valide.');
      // Gérer l'erreur ici, par exemple rediriger l'utilisateur vers une page d'erreur
      return;
  }

  // Récupérer les valeurs du formulaire
  var title = document.getElementById('title').value;
  var content = document.getElementById('content').value;

  // Vérifier que les champs requis ne sont pas vides
  if (!title || !content) {
      alert('Veuillez remplir tous les champs.');
      return;
  }

  // Construire l'objet question
  var questionData = {
      "title": title,
      "id_user": userId, // Utiliser l'ID de l'utilisateur
      "date": "", // Ajouter la date actuelle
      "content": content
  };

  console.log('Données envoyées à l\'API :', questionData);

  // Options de la requête fetch
  var requestOptions = {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json'
      },
      body: JSON.stringify(questionData)
  };

  // URL de l'API
  var apiEndpoint = 'http://localhost/GameApp/questions/create'; 

  // Effectuer la requête fetch
  fetch(apiEndpoint, requestOptions)
      .then(function(response) {
          if (!response.ok) {
              return response.json().then(function(err) {
                  throw new Error('Erreur HTTP ' + response.status + ': ' + (err.message || 'Réponse incorrecte du serveur'));
              });
          }
          return response.json();
      })
      .then(function(data) {
          console.log('Réponse de l\'API :', data);

          // Traiter la réponse de l'API ici
          alert('Question ajoutée !');
          document.getElementById('submitQuestionForm').reset();
          window.location.reload();
      })
      .catch(function(error) {
          console.error('Erreur lors de la requête :', error);
          // Gérer les erreurs ici, par exemple affichage d'un message d'erreur à l'utilisateur
          alert('Erreur lors de l\'ajout de la question : ' + error.message); // Exemple d'une alerte d'erreur
      });
});

document.getElementById('submitUpdateProfileForm').addEventListener('submit', async function(event) {
  event.preventDefault(); // Empêche le comportement par défaut du formulaire

  // Récupère les données du formulaire
  const username = document.getElementById('username').value;
  const profilePicture = document.getElementById('profilePicture').files[0];

  try {
      // Télécharge l'image et obtient l'URL depuis Cloudinary
      const imageUrl = await uploadImageToCloudinary(profilePicture);
      const email = localStorage.getItem('email'); // Récupère l'email depuis le localStorage

      // Affichage des valeurs dans la console
      console.log('Username:', username);
      console.log('Email:', email);
      console.log('Profile Picture URL:', imageUrl);

      // Prépare les données à envoyer à l'API
      const data = {
          mail: email,
          username: username,
          password: "1234", // Assurez-vous que cela est sécurisé côté serveur
          urlPictureProfil: imageUrl // Utilise l'URL sécurisée de l'image téléchargée
      };

      // URL de l'API
      const apiEndpoint = 'http://localhost/GameApp/users/update';

      // Options de la requête fetch
      const requestOptions = {
          method: 'PUT',
          headers: {
              'Content-Type': 'application/json'
          },
          body: JSON.stringify(data)
      };

      // Envoie les données à l'API pour mettre à jour le profil
      const response = await fetch(apiEndpoint, requestOptions);

      if (response.ok) {
          console.log('Profile updated successfully');
      } else {
          console.error('An error occurred while updating the profile');
      }
  } catch (error) {
      console.error('An error occurred:', error);
  }
});

// Fonction pour télécharger l'image vers Cloudinary et obtenir l'URL sécurisée
async function uploadImageToCloudinary(file) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('upload_preset', 'gamedbpreset'); // Remplacez par votre upload preset

  const response = await fetch('https://api.cloudinary.com/v1_1/deqqta8v0/image/upload', {
      method: 'POST', // Utilisation de la méthode POST pour l'upload d'image
      body: formData
  });

  if (response.ok) {
      const result = await response.json();
      return result.secure_url; // Retourne l'URL sécurisée de l'image
  } else {
      throw new Error('Failed to upload image');
  }
}
