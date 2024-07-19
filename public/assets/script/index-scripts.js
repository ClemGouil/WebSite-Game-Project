document.addEventListener('DOMContentLoaded', function() {
  updateProfileInfo();
  showPage('home'); // Affiche la page d'accueil par défaut
  checkLoginStatus(); // Vérifie l'état de connexion de l'utilisateur
  fetchQuestions();

  const notifImage = document.getElementById('notif');

  notifImage.addEventListener('click', function() {
    // Add the logic for the notification click event
  });

  const cloudName = "hzxyensd5"; // replace with your own cloud name
  const uploadPreset = "aoh4fpwm"; // replace with your own upload preset

  const myWidget = cloudinary.createUploadWidget(
    {
      cloudName: cloudName,
      uploadPreset: uploadPreset,
      // Additional options can be added here
    },
    (error, result) => {
      if (!error && result && result.event === "success") {
        console.log("Done! Here is the image info: ", result.info);
        document.getElementById("uploadedimage").setAttribute("src", result.info.secure_url);
        document.getElementById("profilePictureUrl").value = result.info.secure_url;
        document.getElementById("uploadedimage").style.display = "block";
      } else if (error) {
        console.error("Upload Widget error: ", error);
      }
    }
  );

  document.getElementById("upload_widget").addEventListener("click", function() {
    myWidget.open();
  }, false);

  // Handle form submission
  document.getElementById('submitUpdateProfileForm').addEventListener('submit', async function(e) {
    e.preventDefault(); // Prevent default form submission

    const username = document.getElementById('username').value;
    const profilePictureUrl = document.getElementById('profilePictureUrl').value;
    const email = localStorage.getItem('email');

    if (!profilePictureUrl) {
      alert('Please upload a profile picture.');
      return;
    }

    console.log('Username:', username);
    console.log('Email:', email);
    console.log('Profile Picture URL:', profilePictureUrl);

    const data = {
      mail: email,
      username: username,
      password: "1234", // Assurez-vous que cela est sécurisé côté serveur
      urlPictureProfil: profilePictureUrl // Utilise l'URL sécurisée de l'image téléchargée
    };

    const apiEndpoint = 'http://localhost/GameApp/users/update';

    try {
      const response = await fetch(apiEndpoint, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      if (response.ok) {
        console.log('Profile updated successfully');
        // Update local storage
        localStorage.setItem('username', username);
        localStorage.setItem('urlPictureProfil', profilePictureUrl);
        alert('Profile updated successfully!');
        // Optionally reload the page or update the UI
        updateProfileInfo();
        window.location.reload();
      } else {
        console.error('An error occurred while updating the profile');
        alert('An error occurred while updating the profile');
      }
    } catch (error) {
      console.error('An error occurred:', error);
      alert('An error occurred while updating the profile');
    }
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
  const isLoggedIn = localStorage.getItem('isLogged') === 'true';
  console.log('isLoggedIn:', isLoggedIn);

  const loginButton = document.getElementById('login-button');
  const profile = document.getElementById('profile');
  const notif = document.getElementById('notif');
  const profileMenu = document.getElementById('profile-menu');

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
  const username = localStorage.getItem('username');
  const email = localStorage.getItem('email');
  const urlPictureProfil = localStorage.getItem('urlPictureProfil');

  console.log('Updated Info', username, email);
  document.getElementById('profile-username-menu').textContent = username || 'Nom non défini';
  document.getElementById('profile-email-menu').textContent = email || 'Email non défini';
  document.getElementById('profile-image').src = urlPictureProfil || 'assets/ressource/profile.jpg';
}

function toggleProfileMenu() {
  const profileMenu = document.getElementById('profile-menu');
  if (profileMenu.style.display === 'block') {
    profileMenu.style.display = 'none';
  } else {
    updateProfileInfo();
    profileMenu.style.display = 'block';
  }
}

function editProfile() {
  showPage("update-profile");
  toggleProfileMenu();
}

function logout() {
  localStorage.removeItem('username');
  localStorage.removeItem('email');
  localStorage.setItem('isLogged', false);
  window.location.href = 'index.html';
  toggleProfileMenu();
}

async function fetchQuestions() {
  try {
    const apiEndpoint = 'http://localhost/GameApp/questions'; 
    const response = await fetch(apiEndpoint);
    const questions = await response.json();
    displayQuestions(questions);
  } catch (error) {
    console.error('Erreur lors de la récupération des questions:', error);
  }
}

async function fetchAnswers() {
  try {
    const questionIdString = localStorage.getItem('questionId');
    const questionId = parseInt(questionIdString, 10);
    const apiEndpoint = 'http://localhost/GameApp/answers/' + questionId; 
    const response = await fetch(apiEndpoint);
    const answers = await response.json();
    displayAnswers(answers);
  } catch (error) {
    console.error('Erreur lors de la récupération des answers:', error);
  }
}

async function getUserById(id) {
  try {
    const apiEndpoint = 'http://localhost/GameApp/users/' + id; 
    const response = await fetch(apiEndpoint);
    if (response.ok) {
      return await response.json();
    } else {
      throw new Error('User not found');
    }
  } catch (error) {
    console.error('Erreur lors de la récupération user', error);
    throw error;
  }
}

async function displayQuestions(questions) {
  const container = document.getElementById('questions-container');
  for (const question of questions) {
    try {
      const user = await getUserById(question.id_user);
      const username = user.username;
      const urlPictureProfil = user.urlPictureProfil;
      createQuestionCard(container, question, username, urlPictureProfil);
    } catch (error) {
      console.error(`Error fetching user with ID ${question.id_user}:`, error);
      const username = 'Utilisateur inconnu';
      createQuestionCard(container, question, username, "assets/ressource/Profile.jpg");
    }
  }
}

async function displayAnswers(answers) {
  const container = document.getElementById('answers-container');
  container.innerHTML = ''; // Clear existing content

  for (const answer of answers) {
    try {
      const user = await getUserById(answer.id_user);
      const username = user.username;
      const urlPictureProfil = user.urlPictureProfil;
      createAnswerCard(container, answer, username, urlPictureProfil);
    } catch (error) {
      console.error(`Erreur lors de la récupération de l'utilisateur avec l'ID ${answer.id_user}:`, error);
      const username = 'Utilisateur inconnu';
      createAnswerCard(container, answer, username, 'assets/ressource/Profile.jpg'); // Default profile picture
    }
  }
}

function createAnswerCard(container, answer, username, urlPictureProfil) {
  const card = document.createElement('div');
  card.className = 'answer-card';

  card.innerHTML = `
    <img src="${urlPictureProfil}" alt="Photo de profil" class="profile-pic">
    <div class="answer-content">
      <div class="username">${username}</div>
      <p>${answer.content}</p>
      <div class="answer-date">${answer.date}</div>
    </div>
  `;

  container.appendChild(card);
}


function createQuestionCard(container, question, username, urlPictureProfil) {
  const card = document.createElement('div');
  card.className = 'question-card';

  card.innerHTML = `
    <img src=${urlPictureProfil} alt="Photo de profil" class="profile-pic">
    <div class="question-content">
      <div class="username">${username}</div>
      <h3>${question.title}</h3>
      <p>${truncateText(question.content, 100)}</p>
    </div>
  `;
  card.addEventListener('click', () => {
    showPage("question-details");
    document.getElementById('question-username').innerText = username;
    document.getElementById('question-title').innerText = question.title;
    document.getElementById('question-description').innerText = question.content;
    document.getElementById('question-profile-pic').src = urlPictureProfil;

    var getIdApiEndpoint = `http://localhost/GameApp/questions/id/${question.title}/${question.id_user}`;

    fetch(getIdApiEndpoint)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erreur HTTP ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            // Supposons que l'ID de la question soit renvoyé directement dans la réponse JSON
            const questionId = data; // Si `data` est l'ID directement
            // Si `data` est un objet contenant l'ID, utilisez : `const questionId = data.id;`
            console.log('ID de la question :', questionId);
            localStorage.setItem('questionId',questionId);

            // Vous pouvez maintenant utiliser `questionId` selon vos besoins
        })
        .catch(error => {
            console.error('Erreur lors de la requête API :', error);
            alert('Erreur lors de la requête API : ' + error.message);
        });
        fetchAnswers();

});
  container.appendChild(card);
}

function truncateText(text, maxLength) {
  if (text.length > maxLength) {
    return text.substring(0, maxLength) + '...';
  }
  return text;
}

document.getElementById('submitQuestionForm').addEventListener('submit', function(event) {
  event.preventDefault();

  const isLogged = localStorage.getItem('isLogged');
  if (isLogged !== 'true') {
    alert('Veuillez vous connecter pour soumettre une question.');
    window.location.href = 'login.html';
    return;
  }

  const userIdString = localStorage.getItem('userId');
  const userId = parseInt(userIdString, 10);

  if (isNaN(userId)) {
    console.error('Erreur : userId n\'est pas un nombre valide.');
    return;
  }

  const title = document.getElementById('title').value;
  const content = document.getElementById('content').value;

  if (!title || !content) {
    alert('Veuillez remplir tous les champs.');
    return;
  }

  const questionData = {
    "title": title,
    "id_user": userId,
    "date": "",
    "content": content
  };

  console.log('Données envoyées à l\'API :', questionData);

  const requestOptions = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(questionData)
  };

  const apiEndpoint = 'http://localhost/GameApp/questions/create';

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
      alert('Question ajoutée !');
      document.getElementById('submitQuestionForm').reset();
      window.location.reload();
    })
    .catch(function(error) {
      console.error('Erreur lors de la requête API :', error);
      alert('Erreur lors de la requête API : ' + error.message);
    });
});


document.getElementById('submitAnswerForm').addEventListener('submit', function(event) {
  event.preventDefault();

  const isLogged = localStorage.getItem('isLogged');
  if (isLogged !== 'true') {
    alert('Veuillez vous connecter pour soumettre une réponse.');
    window.location.href = 'login.html';
    return;
  }

  const userIdString = localStorage.getItem('userId');
  const userId = parseInt(userIdString, 10);
  const questionIdString = localStorage.getItem('questionId');
  const questionId = parseInt(questionIdString, 10);

  if (isNaN(userId)) {
    console.error('Erreur : userId n\'est pas un nombre valide.');
    return;
  }

  const content = document.getElementById('answer').value;

  if (!content) {
    alert('Veuillez remplir tous les champs.');
    return;
  }

  const answerData = {
    "id_question": questionId,
    "id_user": userId,
    "date": "",
    "content": content
  };

  console.log('Données envoyées à l\'API :', answerData);

  const requestOptions = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(answerData)
  };

  const apiEndpoint = 'http://localhost/GameApp/answers/create';

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
      alert('Answer ajoutée !');
      document.getElementById('submitAnswerForm').reset();
      window.location.reload();
    })
    .catch(function(error) {
      console.error('Erreur lors de la requête API :', error);
      alert('Erreur lors de la requête API : ' + error.message);
    });
});

