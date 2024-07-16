document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Empêche le comportement par défaut du formulaire

    // Récupérer les valeurs du formulaire
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;

    // Construire l'objet utilisateur pour le login
    var userData = {
        username: "",
        mail: email,
        password: password
    };

    // Options de la requête fetch pour le login
    var loginRequestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    };

    // URL de l'API pour le login
    var loginApiEndpoint = 'http://localhost/GameApp/users/login';

    // Effectuer la requête fetch pour le login
    fetch(loginApiEndpoint, loginRequestOptions)
        .then(function(response) {
            if (!response.ok) {
                throw new Error('Erreur HTTP ' + response.status);
            }
            return response.json();
        })
        .then(function(data) {
            console.log('Réponse de l\'API de login :', data);

            // Stocker les informations de l'utilisateur dans le localStorage
            localStorage.setItem('isLogged', 'true');
            localStorage.setItem('username', data.username);
            localStorage.setItem('email', data.mail);

            // Effectuer une requête fetch pour obtenir l'ID de l'utilisateur
            var getIdApiEndpoint = `http://localhost/GameApp/users/id/${email}`;

            return fetch(getIdApiEndpoint)
                .then(function(response) {
                    if (!response.ok) {
                        throw new Error('Erreur HTTP ' + response.status);
                    }
                    // La réponse attendue est juste un texte brut contenant l'ID
                    return response.text(); // Récupérer le texte brut de la réponse
                })
                .then(function(idText) {
                    // Convertir le texte en nombre entier
                    var userId = parseInt(idText, 10);

                    // Vérifier si userId est un nombre valide
                    if (isNaN(userId)) {
                        throw new Error('Réponse API invalide : ID non trouvé');
                    }

                    // Stocker l'ID de l'utilisateur dans le localStorage
                    localStorage.setItem('userId', userId);

                    alert('Login réussi ! ID utilisateur : ' + userId);

                    // Rediriger l'utilisateur vers la page d'accueil
                    window.location.href = 'index.html';
                });
        })
        .catch(function(error) {
            console.error('Erreur lors de la requête :', error);
            alert('Erreur lors du login : ' + error.message);
        });
});