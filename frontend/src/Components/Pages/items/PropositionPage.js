import { sendFile } from '../../../utils/fileUpload';
import { clearPage } from '../../../utils/render';
import Navbar from '../../Navbar/Navbar';
import Navigate from '../../Router/Navigate';
import { isAuthenticated, getAuthenticatedUser } from '../../../utils/auths';

const PropositionPage = async () => {
  clearPage();
  renderPropositionPage();
};

async function renderPropositionPage() {
  const main = document.querySelector('main');

  const formString = await renderForm();

  main.innerHTML += formString;

  const form = document.querySelector('#addItemForm');
  const phoneWrapper = document.querySelector('#phoneWrapper');

  if (!isAuthenticated()) {
    const str = `
                  <label class="col-form-label mt-4" for="inputDefault">Numéro de téléphone:</label>
                    <input type="text" class="form-control" id="phoneNumber" name = "phoneNumber" required>
                `;

    const div = document.createElement('div');
    div.className = 'form-group ';
    div.innerHTML = str;

    phoneWrapper.appendChild(div)
  
  }
  form.addEventListener('submit', onAddItem);
}
async function renderForm() {
  const formString = `<h1 class="text-center" style="margin-top: 100px;">Proposez votre objet!</h1>
  <div class="container py-3 border border-2 w-75 border-primary rounded rounded-5 shadow-lg mb-5">
  <form id="addItemForm">
      <div class="row">

          <div class="col-4">
              <div class="form-group">  
                  <label for="dateParkArrival" class="form-label mt-4">Date de venu au parc</label>
                  <input type="date" class="form-control" id="dateParkArrival" name="dateParkArrival" list="date-list">
                  ${await getAvailabilitiesAsString()}
              </div>
          </div>

          <div class="col-4">
              <div class="form-group">

                  <label for="timeSlot" class="form-label mt-4">Plage horaire</label>
                  <select class="form-select" id="timeSlot" name="timeSlot">
                    <option value="11:00-13:00">11:00 - 13:00</option>
                    <option value="14:00-16:00">14:00 - 16:00</option>
                  </select>
              </div>
          </div>

          <div class="col-4">
              <div class="form-group">
                  <label for="itemType" class="form-label mt-4">Le type d'objet:</label>
                  ${await getAllItemTypeAsString()}
              </div>
          </div>

      </div>

      <div class="row">
          <div class="col">
              <div class="form-group">
                  <label for="description"class="form-label mt-4">Description:</label>
                  <textarea class="form-control" iid="description" name="description" rows="3"></textarea>
              </div>
          </div>

          <div class="col-5">
            <div id ="phoneWrapper">
                
            </div>
          </div>
      </div>

      <div class="row justify-content-center">
          <div class="col-8">
              <div class="form-group">
                  <label for="formFile" class="form-label mt-4">Photo de votre objet</label>
                  <input class="form-control" name="file" type="file" id="formFile">
              </div>
          </div>
      </div>

      <div class="row py-3">
          <div class="col text-center">
          <button  type="submit" class="btn btn-primary">Proposer l'objet</button>
          </div>
      </div>

      </form>
  </div>`;

  return formString;
}

async function getAvailabilitiesAsString() {
  const avs = await getAvailabilitiesFromAPI();

  let str = `<datalist id="date-list">`;

  avs.forEach(av => {
    const year = av[0];
    const month = av[1];
    const day = av[2];

    const date = `${year}-${month.toString().padStart(2, "0")}-${day.toString().padStart(2, "0")}`;// AAAA/MM/JJ


    str+=`<option value="${date}"></option>`
  });

  str += `</datalist>`;

  return str;
}

async function getAvailabilitiesFromAPI() {
  const response = await fetch(`api/items/availabilities`);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const availabilities = await response.json();
  return availabilities;
}

async function onAddItem(e) {
  e.preventDefault();

  const namePicture = await sendFile();
  const picture = namePicture.fileName;

  const form = e.target;

  const body = {
    itemType: form.elements.itemType.value,
    description: form.elements.description.value,
    picture,
    timeSlot: form.elements.timeSlot.value,
    dateParkArrival: form.elements.dateParkArrival.value,
  };

  if (!isAuthenticated()) {
    body.phoneNumber = form.elements.phoneNumber.value;
  }
  const options = {
    method: 'POST',
    body: JSON.stringify(body),
    headers: {
      'Content-Type': 'application/json',
    },
  };
  let response;
  if (!isAuthenticated()) {
    response = await fetch(`api/items`, options);
  } else {
    options.headers.authorization = getAuthenticatedUser().token;
    response = await fetch(`api/items/userisconnected`, options);
  }

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);

  const itemAdded = await response.json();

  // eslint-disable-next-line no-console
  console.log({ itemAdded });

  Navbar();

  Navigate(`/item?id=${itemAdded.id}`);
}

async function getAllItemTypeAsString() {
  const itemsTypesFromAPI = await getAllItemTypeFromAPI();

  let string = `<select class="form-select" id="itemType" name="itemType">`;

  itemsTypesFromAPI.forEach((type) => {
    string += `<option value="${type.label}">${type.label}</option>`;
  });

  string += `</select>`;
  return string;
}

async function getAllItemTypeFromAPI() {
  const response = await fetch(`api/items/types`);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const allUsers = await response.json();
  return allUsers;
}

export default PropositionPage;
