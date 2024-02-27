import { clearPage } from '../../../utils/render';
import { getAuthenticatedFromApi, getAuthenticatedUser } from '../../../utils/auths';
import { getPictureFromAPI } from '../../../utils/fileUpload';
import Navigate from '../../Router/Navigate';
import Navbar from '../../Navbar/Navbar';

const EditProfilePage = async () => {
  clearPage();
  renderProfilePage();
};

async function renderProfilePage() {
  const user = await getAuthenticatedFromApi();

  const main = document.querySelector('main');

  const year = user.registrationDate[0];
  const month = user.registrationDate[1];
  const day = user.registrationDate[2];

  const dateInscription = `${day.toString().padStart(2, '0')}/${month
    .toString()
    .padStart(2, '0')}/${year}`;

  const pictureURI = await getPictureFromAPI(user.profilePicture);
  const string = ` <h1 class="text-center" style="margin-top: 150px;">Modifier mon profil </h1> <p id="userId" hidden>${user.userId}</p> 
  <div class="container py-3 border border-2 w-75 border-primary rounded rounded-5 shadow-lg mb-5">
      <div class="row">

          <div class="col-3 border-primary border-end text-center my-auto">
              <img src="${pictureURI}" width="100%"  alt="...">
          </div>

          <div class="col my-auto">
              <div class="container-fluid">

                  <div class="row">
                      <div class="col">
                          <h5>Date inscription:  ${dateInscription}</h5> 
                      </div>

                      <div class="col">
                          <h5>Rôle: ${user.role}</h5>
                      </div>
                  </div>

            
                  <div class="row">
                      <div class="col">
                          <div class="form-group">
                              <label class="col-form-label mt-4" for="last-name-input">Nom:</label>
                              <input type="text" class="form-control"  id="last-name-input" value="${user.lastName}">
                          </div>
                      </div>

                      <div class="col">
                          <div class="form-group">
                              <label class="col-form-label mt-4" for="first-name-input">Prénom:</label>
                              <input type="text" class="form-control" id="first-name-input" value="${user.firstName}">
                          </div>
                      </div>
                  </div>
                
                  <div class="row">
                      <div class="col">
                          <div class="form-group">
                              <label for="email-input" class="form-label mt-4">Email:</label>
                              <input type="email" class="form-control" id="email-input" value="${user.email}" aria-describedby="emailHelp" placeholder="Enter email">
                              <small id="emailHelp" class="form-text text-muted">Nous ne communiquerons jamais votre mail à qui que ce soit.</small>
                          </div>
                      </div>

                      <div class="col">
                          <div class="form-group">
                              <label class="col-form-label mt-4" for="mobile-number-input">Numéro de téléphone:</label>
                              <input type="text" class="form-control"  id="mobile-number-input" value="${user.mobileNumber}">
                          </div>
                      </div>

              
                  
                  <div class="row">
                      <div class="col-5">
                      <div class="form-group">
                              <label for="password-input" class="form-label mt-4">Mot de passe actuel:</label>
                              <input type="password" class="form-control" class="form-control" id="password-input" name="password">
                          </div>
                          
                      </div>
                  </div>

                
                  <div class="row">
                      <div class="col-6">
                      <div class="form-group">
                          <label for="exampleInputPassword1" class="form-label mt-4">Nouveau mot de passe (facultatif):</label>
                          <input type="password" class="form-control" id="newPassword" name="password">
                      </div>
                      </div>

                      <div class="col-6">
                          <div class="form-group">
                              <label for="confirmedPassword" class="form-label mt-4">Confirmer le nouveau mot de passe :</label>
                              <input type="password" class="form-control" id="confirmedPassword" >
                          </div>
                      </div>
                  </div>

                  <div id="errorPassword"></div>
          
          </div>
      </div>

      <div class="row py-3">
          <div class="col text-center">
              <button class="btn btn-primary" id ="btnSave">Modifier mon Profil</button>
          </div>
      </div>
      


  </div> 
  

  
  `;

  main.innerHTML = string;

  const bouton = document.querySelector('#btnSave');

  bouton.addEventListener('click', updateProfile);
}

async function updateProfile(e) {
  e.preventDefault();
  const lastName = document.getElementById('last-name-input').value;
  const firstName = document.getElementById('first-name-input').value;
  const email = document.getElementById('email-input').value;
  const mobileNumber = document.getElementById('mobile-number-input').value;
  const oldPassword = document.getElementById('password-input').value;
  // const profilePicturePath = document.getElementById("profilePicturePath").t;
  const userId = document.getElementById('userId').innerText;
  const newPassword = document.getElementById('newPassword').value;

  if (newPassword.length > 0) {
    const confirmedPasswordInput = document.getElementById('confirmedPassword').value;
    if (newPassword !== confirmedPasswordInput) {
      const error  = document.querySelector('#errorPassword');


      error.innerHTML=`<div class="col">
      <div class="alert alert-dismissible alert-danger">


          <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            Les deux mots de passe ne sont pas identiques
      </div>
  </div>`;
      return;
    }
  }

  const options = {
    method: 'PATCH',
    body: JSON.stringify({
      userId,
      lastName,
      firstName,
      email,
      mobileNumber,
      oldPassword,
      password: newPassword
    }),
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`api/users`, options);
  if (!response.ok) {
    const errorText = await response.text();
    const wrapper = document.getElementById('errorPassword');
    wrapper.innerHTML = `
    <div class="col">
      <div class="alert alert-dismissible alert-danger">


          <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
          ${errorText}
      </div>
  </div>
    
    
    
  `;
    throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  } else {
    Navbar();
    Navigate('profile');
  }
}


export default EditProfilePage;
