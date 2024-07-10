// index-scripts.js
document.addEventListener('DOMContentLoaded', function() {
  showPage('home'); // Affiche la page d'accueil par défaut
  checkLoginStatus(); // Vérifie l'état de connexion de l'utilisateur

  const profileImage = document.getElementById('profile');
  const profileInfo = document.getElementById('profile-info');

  profileImage.addEventListener('click', function() {
    showPage("profile-info")
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
  // Simuler l'état de connexion de l'utilisateur
  var isLoggedIn = false; // Changez cette valeur pour tester

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