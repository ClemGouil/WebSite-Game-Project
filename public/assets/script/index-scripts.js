// index-scripts.js
document.addEventListener('DOMContentLoaded', function() {
  showPage('home'); // Affiche la page d'accueil par défaut
  checkLoginStatus(); // Vérifie l'état de connexion de l'utilisateur

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
  // Redirige vers la page de modification de profil
  window.location.href = 'edit-profile.html';
}

function logout() {
  // Logic to logout the user
  localStorage.removeItem('username');
  localStorage.removeItem('email');
  localStorage.setItem('isLogged',false);
  window.location.href = 'index.html';
}

