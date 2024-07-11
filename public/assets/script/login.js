document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Empêche le comportement par défaut du formulaire

    // Récupérer les valeurs du formulaire
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value; 

    // Construire l'objet utilisateur
    var userData = {
      username: "",
      mail: email,
      password: password
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
    var apiEndpoint = 'http://localhost/GameApp/users/login'; 

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
        
        localStorage.setItem('isLogged', 'true');
        localStorage.setItem('username', data.username);
        localStorage.setItem('email', data.mail);
        alert('Login réussie !'); 

        window.location.href = 'index.html'
      })
      .catch(function(error) {
        console.error('Erreur lors de la requête :', error);
        alert('Erreur lors du login : ' + error.message);
      });
  });