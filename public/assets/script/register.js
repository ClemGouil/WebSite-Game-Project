document.getElementById('registrationForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Empêche le comportement par défaut du formulaire

    // Récupérer les valeurs du formulaire
    var username = document.getElementById('username').value;
    var email = document.getElementById('email').value;
    var confirmPassword = document.getElementById('confirm-password').value; 

    // Construire l'objet utilisateur
    var userData = {
      username: username,
      mail: email,
      password: confirmPassword,
      urlPictureProfil : "images/profiles/Profile.jpg"
    };

    // Options de la requête fetch
    var requestOptions = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(userData)
    };

    // URL de mon API
    var apiEndpoint = 'http://localhost/GameApp/users/register'; 

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
        alert('Inscription réussie !'); 
        // Vous pouvez rediriger l'utilisateur ou effectuer d'autres actions après l'inscription
         window.location.href = 'login.html'
      })
      .catch(function(error) {
        console.error('Erreur lors de la requête :', error);
        // Gérez les erreurs ici, par exemple affichage d'un message d'erreur à l'utilisateur
        alert('Erreur lors de l\'inscription : ' + error.message); // Exemple d'une alerte d'erreur
      });
  });