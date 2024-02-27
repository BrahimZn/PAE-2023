import { clearPage } from '../../../utils/render';
import { setAuthenticatedUser, setRememberMe } from '../../../utils/auths';
import Navbar from '../../Navbar/Navbar';
import Navigate from '../../Router/Navigate';

const ConnexionPage = () => {
  clearPage();
  renderConnexionPage();
};

function renderConnexionPage() {
  const main = document.querySelector('main');

  const formString = renderForm();

  main.innerHTML += formString;

  const form = document.querySelector('#formConnexion');

  form.addEventListener('submit', onLogin);

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
  <form id="formConnexion" class="container border border-2 rounded-5 w-50 border-primary my-5  shadow-lg p-3 mb-5 bg-personnalisee rounded">
  <div class="row">
      <h1 class="text-center">Connexion</h1>
  </div>

  <div class="row justify-content-center ">

      <div class="col-8 ">
          <div class="mb-3 ">
              <label for="email" class="form-label">Email :</label>
              <input type="email" class="form-control" id="email" name="email" required
                  placeholder="Entrez votre email">
              <small id="emailHelp" class="form-text text-muted">Nous ne partagerons jamais votre email avec qui
                  que ce soit.</small>
          </div>
      </div>

  </div>

  <div class="row justify-content-center  ">
      <div class="col-8">
          <div class="mb-3 ">
              <label for="password" class="form-label">Mot de passe :</label>
              <input type="password" class="form-control" id="password" name="password" required  placeholder="Entrez votre mot de passe">
              <button type="button" id="show-password" class="btn btn-primary">Afficher le mot de passe</button>
          </div>
      </div>

  </div>

  <div class="row justify-content-center">
      <div class="col-8">
          <div class="mb-3 form-check">
              <input type="checkbox" class="form-check-input" id="remember-me">
              <label class="form-check-label" for="remember-me">Se souvenir de moi</label>
          </div>
      </div>

  </div>

  <div class="row justify-content-center">
      <div class="col-3">
          <button type="submit" class="btn btn-primary">Se connecter</button>
      </div>

  </div>


</form>`;

  return formString;
}

async function onLogin(e) {
  e.preventDefault();

  const email = document.querySelector('#email').value;
  const password = document.querySelector('#password').value;
  const rememberMeCheckbox = document.querySelector('#remember-me');

  if (rememberMeCheckbox.checked) {
    setRememberMe(true);
  }
  const options = {
    method: 'POST',
    body: JSON.stringify({
      login: email,
      password,
    }),
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const response = await fetch(`api/auths/login`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);

  const authenticatedUser = await response.json();

  // eslint-disable-next-line no-console
  console.log('authenticated user : ', authenticatedUser);

  setAuthenticatedUser(authenticatedUser);

  Navbar();

  Navigate('/');
}

export default ConnexionPage;
