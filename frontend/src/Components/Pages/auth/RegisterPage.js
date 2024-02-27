/* eslint-disable no-alert */
import { clearPage } from '../../../utils/render';
import {sendFile} from '../../../utils/fileUpload';
import Navigate from '../../Router/Navigate';

const RegisterPage = () => {
  clearPage();
  renderRegisterPage();
};

function renderRegisterPage() {
  const main = document.querySelector('main');

  const formString = renderForm();

  main.innerHTML += formString;

  const form = document.querySelector('#formRegister');

  form.addEventListener('submit', onRegister);

  const passwordInput = document.getElementById('password');
  const showPasswordButton = document.getElementById('show-password');

  showPasswordButton.addEventListener('click', () => {
    if (passwordInput.type === 'password') {
      passwordInput.type = 'text';
      showPasswordButton.textContent = 'Masquer le mot de passe';
    } else {
      passwordInput.type = 'password';
      showPasswordButton.textContent = 'Afficher le mot de passe';
    }
  });

  passwordInput.addEventListener('input', () => {
    if (passwordInput.value.length > 0) {
      showPasswordButton.style.display = 'block';
    } else {
      showPasswordButton.style.display = 'none';
    }
  });
}
function renderForm() {
  const formString = `
<h1 style="margin-top: 150px;"></h1>
  <form id="formRegister"
        class="container border border-2 rounded-5 w-50 border-primary my-5  shadow-lg p-3 mb-5 bg-personnalisee rounded" >
        <div class="row">
            <h1 class="text-center" >Inscription</h1>
        </div>

        <div class="row justify-content-around ">
            <div class="col-4 ">
                <div class="mb-3 ">
                    <label for="lastName" class="form-label">Nom :</label>
                    <input type="text" class="form-control" id="lastName" name="lastName" required
                        placeholder="Entrez votre nom">
                </div>
            </div>

            <div class="col-4 ">
                <div class="mb-3 ">
                    <label for="firstName" class="form-label">Prénom :</label>
                    <input type="text" class="form-control" id="firstName" name="firstName" required
                        placeholder="Entrez votre prénom">
                </div>
            </div>
        </div>

        <div class="row justify-content-center">
            <div class="col-6 ">
                <div class="mb-3 ">
                    <label for="email" class="form-label">Email :</label>
                    <input type="email" class="form-control" id="email" name="email" required
                        placeholder="Entrez votre email">
                </div>
            </div>

            <div class="col-6">
                <div class="mb-3 ">
                    <label for="number" class="form-label">Numéro de téléphone :</label>
                    <input type="tel" class="form-control" id="number" name="number" required
                        placeholder="Entrez votre numéro de téléphone">
                </div>
            </div>
        </div>

        <div class="row justify-content-center">
            <div class="col-10">
                <div class="mb-3">
                    <label for="formFile" class="form-label mt-4">Votre image ou avatar : </label>
                    <input class="form-control" name="file" type="file" id="formFile">
                </div>
            </div>
        </div>


        </div>

        <div class="row justify-content-center">
            <div class="col-6">
                <div class="mb-3 ">
                    <label for="password" class="form-label">Mot de passe :</label>
                    <input type="password" class="form-control" id="password" name="password" required
                        placeholder="Entrez votre mot de passe">
                    <button type="button" id="show-password" class="btn btn-primary">Afficher le mot de
                        passe</button>
                </div>
            </div>

            <div class="col-6">
                <div class="mb-3 ">
                    <label for="confirmedpassword" class="form-label">Mot de passe à confirmer :</label>
                    <input type="password" class="form-control" id="confirmedpassword" name="confirmedpassword"
                        required placeholder="Entrez votre mot de passe de nouveau">
                </div>
            </div>
        </div>

        <div class="row justify-content-start">
            <div class="col">
                <div class="col-9">
                    <div class="mb-3 form-check">
                        <input type="checkbox" class="form-check-input" id="accept-rules">
                        <label class="form-check-label" for="remember-me">J'ai lu et j'accepte les Conditions d'utilisation 
                            et la Politique de confidentialié de RessourceRie.</label>
                    </div>
                </div>
            </div>
        </div>

        <div class="row justify-content-center">
            <div class="col-3">
                <button type="submit" class="btn btn-primary">S'inscrire</button>
            </div>
        </div>

    </form>`;

  return formString;
}

async function onRegister(e) {
  e.preventDefault();
  alert("Inscription en cours ")
  const firstName = document.querySelector('#firstName').value;
  const lastName = document.querySelector('#lastName').value;
  const mobileNumber = document.querySelector('#number').value;

  const email = document.querySelector('#email').value;
  const password = document.querySelector('#password').value;
  const confirmedPassword = document.querySelector('#confirmedpassword').value;
  const acceptRulesCheckbox = document.querySelector('#accept-rules');

  if (password !== confirmedPassword) {
    alert('Votre mot de passe ne correspond pas à la confirmation de mot de passe');
    return;
  }

  if (!acceptRulesCheckbox.checked) {
    alert("Vous devez accepter les conditions d'utilisation et la politique de confidentialité");
    return;
  }

  const responseFile = await sendFile();
  const profilePicture = responseFile.fileName;
  // eslint-disable-next-line no-console
  console.log(profilePicture);
  const options = {
    method: 'POST',
    body: JSON.stringify({
      lastName,
      firstName,
      email,
      password,
      mobileNumber,
      profilePicture
    }),
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const response = await fetch(`api/auths/register`, options);

  if (!response.ok) {
    alert('Votre inscription à échoué');
    throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  } else {
    alert("Votre inscription s'est déroulé avec succès !");
  }

  Navigate('/connexion');
}

export default RegisterPage;
