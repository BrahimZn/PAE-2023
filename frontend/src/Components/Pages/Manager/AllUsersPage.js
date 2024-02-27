import Navbar from '../../Navbar/Navbar';
import { getAuthenticatedUser } from '../../../utils/auths';

const AllUsersPage = async () => {
  Navbar();
  const main = document.querySelector('main');
  main.innerHTML = await getAllUsersAsString();
  attachBtnListenerForHelpers();
  attachBtnListenerForMembers();
  attachBtnListenerForManagers();
  attachBtnListenerForManagersToHelpers();
};

async function getAllUsersAsString() {
  const resultUsersFromAPI = await getAllUsersFromAPI();
  const resultHelpersFromAPI = await getAllUsersHelpersFromAPI();
  const resultManagersFromAPI = await getAllUsersManagersFromAPI();

  let allUsersString = `
  <h1 class="text-center py-3" style="margin-top: 100px;"> Gestion des utilisateurs </h1>
  
  `
  allUsersString += `
    <ul class=" nav nav-tabs"  id="tabPersonnalise"role="tablist">
        <li class=" nav-item" role="presentation">
            <a class="nav-link active" data-bs-toggle="tab" href="#members" aria-selected="true" role="tab">
                <h2>Membres</h2>
            </a>
        </li>
        <li class=" nav-item" role="presentation">
            <a class="nav-link" data-bs-toggle="tab" href="#helpers" aria-selected="false" role="tab" tabindex="-1">
                <h2>Aidants</h2>
            </a>
        </li>

        <li class=" nav-item" role="presentation">
            <a class="nav-link" data-bs-toggle="tab" href="#managers" aria-selected="false" role="tab" tabindex="-2">
                <h2>Managers</h2>
            </a>
        </li>
    </ul>
  `

  allUsersString += `
  
  <div id="myTabContent" class="tab-content">
        <div class="tab-pane fade active show" id="members" role="tabpanel">

            <div class="container border border-2 rounded rounded-3 border-primary my-3">
                <h1 class="text-center">Liste de tous les membres:</h1>
                <table class="table table-bordered table-hover">
                    <thead class="table-warning">
                        <tr>
                            <th>Nom</th>
                            <th>Prénom</th>
                            <th>Email</th>
                            <th>Numéro de téléphone</th>
                            <th></th>
                            <th></th>
                        </tr>
                    </thead>

                    <tbody class="table-group-divider">
  
  `

  resultUsersFromAPI?.forEach((user) => {
    allUsersString += `<tr>
      <td>${user.lastName}</td>
      <td>${user.firstName}</td>
      <td>${user.email}</td>
      <td class="col-2">${user.mobileNumber}</td>
      <td class="col-2 text-center">
        <button type="button" class="btn btn-primary btnIndicateUserAsHelper" data-user="${user.userId}">Indiquer
          aidant
        </button>
      </td>
      <td class="col-2 text-center">
        <button type="button" class="btn btn-primary btnIndicateUserAsManager" data-user="${user.userId}">Indiquer
          responsable
        </button>
      </td>
    </tr>`
  });

allUsersString += `

          </tbody>
          </table>
      </div>
  </div>

`

  allUsersString += `
  
  <div class="tab-pane fade" id="helpers" role="tabpanel">
            <div class="container border border-2 rounded rounded-3 border-primary my-3">
                <h1 class="text-center">Liste de tous les aidants:</h1>
                <table class="table table-bordered table-hover">
                    <thead class="table-warning">
                        <tr>
                            <th>Nom</th>
                            <th>Prénom</th>
                            <th>Email</th>
                            <th>Numéro de téléphone</th>
                            <th></th>
                            <th></th>
                        </tr>
                    </thead>

                    <tbody class="table-group-divider">
  
  
  `

  resultHelpersFromAPI?.forEach((user) => {

    allUsersString += `
    <tr>
        <td>${user.lastName}</td>
        <td>${user.firstName}</td>
        <td>${user.email}</td>
        <td class="col-2">${user.mobileNumber}</td>
        <td class="col-2 text-center"><button type="button" class="btn btn-primary btnIndicateUserAsMember" data-user="${user.userId}">Indiquer Membre</button></td>
        <td class="col-2 text-center"><button type="button" class="btn btn-primary btnIndicateUserAsManager" data-user="${user.userId}">Indiquer responsable</button></td>
    </tr>
    
    `

  });

  allUsersString += `
  </tbody>
                </table>
            </div>
        </div>
  
  `

  allUsersString += `
  
  <div class="tab-pane fade" id="managers" role="tabpanel">
            <div class="container border border-2 rounded rounded-3 border-primary my-3">
                <h1 class="text-center">Liste de tous les responsables:</h1>
                <table class="table table-bordered table-hover">
                    <thead class="table-warning">
                        <tr>
                            <th>Nom</th>
                            <th>Prénom</th>
                            <th>Email</th>
                            <th>Numéro de téléphone</th>
                            <th></th>
                            <th></th>
                        </tr>
                    </thead>

                    <tbody class="table-group-divider">
  
  `
  resultManagersFromAPI?.forEach((user) => {

    allUsersString +=`
    <tr>
        <td>${user.lastName}</td>
        <td>${user.firstName}</td>
        <td>${user.email}</td>
        <td class="col-2">${user.mobileNumber}</td>
        <td class="col-2 text-center"><button type="button" class="btn btn-primary btnIndicateUserAsMember" data-user="${user.userId}">Indiquer membre</button></td>
        <td class="col-2 text-center"><button type="button" class="btn btn-primary btnIndicateManagerToHelper" data-user="${user.userId}">Indiquer aidant</button></td>
    </tr>
    `

  });

  allUsersString +=`
  </tbody>
                </table>
            </div>
        </div>
    </div>
  `

  return allUsersString;
}

async function getAllUsersFromAPI() {
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`api/users`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const allUsers = await response.json();
  return allUsers;
}

async function getAllUsersHelpersFromAPI() {
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`api/users/helpersList`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const allUsers = await response.json();
  return allUsers;
}

async function getAllUsersManagersFromAPI() {
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`api/users/managersList`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const allUsers = await response.json();
  return allUsers;
}

function attachBtnListenerForHelpers() {
  const allButtonsIndicateUserAsHelper = document.querySelectorAll('.btnIndicateUserAsHelper');

  allButtonsIndicateUserAsHelper.forEach((btn) => {
    btn.addEventListener('click', async (e) => {
      const btnItemClicked = e.target;
      const idUserToIndicateAsHelper = btnItemClicked?.dataset?.user;

      const userUpdated = await indicateUserAsHelper(idUserToIndicateAsHelper);
      // eslint-disable-next-line no-console
      console.log({ userUpdated });
      AllUsersPage();
    });
  });
}

function attachBtnListenerForMembers() {
  const allButtonsIndicateUserAsMembers = document.querySelectorAll('.btnIndicateUserAsMember');

  allButtonsIndicateUserAsMembers.forEach((btn) => {
    btn.addEventListener('click', async (e) => {
      const btnItemClicked = e.target;
      const idUserToIndicateAsMember = btnItemClicked?.dataset?.user;

      const userUpdated = await indicateUserAsMember(idUserToIndicateAsMember);
      // eslint-disable-next-line no-console
      console.log({ userUpdated });
      AllUsersPage();
    });
  });
}

function attachBtnListenerForManagers() {
  const allButtonsIndicateUserAsManagers = document.querySelectorAll('.btnIndicateUserAsManager');

  allButtonsIndicateUserAsManagers.forEach((btn) => {
    btn.addEventListener('click', async (e) => {
      const btnItemClicked = e.target;
      const idUserToIndicateAsManager = btnItemClicked?.dataset?.user;

      const userUpdated = await indicateUserAsManager(idUserToIndicateAsManager);
      // eslint-disable-next-line no-console
      console.log({ userUpdated });
      AllUsersPage();
    });
  });
}

function attachBtnListenerForManagersToHelpers() {
  const allButtonsIndicateUserAsManagers = document.querySelectorAll('.btnIndicateManagerToHelper');

  allButtonsIndicateUserAsManagers.forEach((btn) => {
    btn.addEventListener('click', async (e) => {
      const btnItemClicked = e.target;
      const idUserToIndicateAsHelper = btnItemClicked?.dataset?.user;

      const userUpdated = await revokeManagerToHelper(idUserToIndicateAsHelper);
      // eslint-disable-next-line no-console
      console.log({ userUpdated });
      AllUsersPage();
    });
  });
}



async function indicateUserAsHelper(idUser) {
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`api/users/indicateAsHelper/${idUser}`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const userUpdated = await response.json();
  return userUpdated;
}

async function indicateUserAsManager(idUser) {
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`api/users/indicateAsManager/${idUser}`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const userUpdated = await response.json();
  return userUpdated;
}

async function indicateUserAsMember(idUser) {
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`api/users/indicateAsMember/${idUser}`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const userUpdated = await response.json();
  return userUpdated;
}

async function revokeManagerToHelper(idUser) {
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`api/users/indicateManagerAsHelper/${idUser}`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const userUpdated = await response.json();
  return userUpdated;
}

export default AllUsersPage;
