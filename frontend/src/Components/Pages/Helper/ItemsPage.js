/* eslint-disable no-console */
/* eslint-disable no-alert */
import Navbar from '../../Navbar/Navbar';
import { getPictureFromAPI } from '../../../utils/fileUpload';
import { clearPage } from '../../../utils/render';
import { getAuthenticatedFromApi, getAuthenticatedUser } from '../../../utils/auths';

const statuses = [
  'tout',
  'proposé',
  'refusé',
  "à l'atelier",
  'en magasin',
  'en vente',
  'vendu',
  'retiré de la vente',
  'accepté',
];

let types = [];
const all = 'tout';
// eslint-disable-next-line prefer-const
let currentStates = all;
let currentType = all;
let availabilities = [];

let currentAvaibality = all;

let currentMax;
let currentMin;
const ItemsPage = async () => {
  Navbar();
  clearPage();

  const typesFromApi = await getAllItemTypeFromAPI();

  const typesLabel = typesFromApi.map((t) => t.label);
  types = [];
  types.push(all);

  types = types.concat(typesLabel);

  availabilities = [];

  availabilities.push(all);

  const avFromAPI = await getAvailabilitiesFromAPI();
  availabilities = availabilities.concat(avFromAPI);

  // attachDataList();
  // await getAllItem();

  await renderPage();

  attachBtnListener();
};

async function renderPage() {
  const itemsFromAPI = await getAllItemFromAPI(currentStates);

  const itemAsString = await getAllItemAsString(itemsFromAPI);

  const main = document.querySelector('main');
  const pageAsString = `
  <h1 class="text-center" style="margin-top: 120px;">Gestion d'objets</h1>
      <div class="container-fluid ">

        <div class="row">
          
          ${attachDataList()}
        
        </div> 
        
        <div class="col "  style="padding-left: 17%;">

          <div class="container p-3" id="itemsWrapper"> 
          
            ${itemAsString}
        
          </div>
        </div>
        
        
        
      </div> `;

  main.innerHTML += pageAsString;
  attachDataListListener();
}
async function getAllItemTypeFromAPI() {
  const response = await fetch(`api/items/types`);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const allUsers = await response.json();
  return allUsers;
}

async function getAvailabilitiesFromAPI() {
  const response = await fetch(`api/items/availabilities`);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const availabilitiesJson = await response.json();
  return availabilitiesJson;
}
function attachDataList() {
  // eslint-disable-next-line no-console
  console.log({ currentStates });
  // const main = document.querySelector('main');
  let string = `<div class="col-2 position-fixed border rounded rounded-3 border-primary border-2 px-1">
  <div class="container-fluid">

      <div class="row">
          <div class="col text-center">
              <h3>Recherche:</h3>
          </div>
      </div>
      <div class="row">
          <div class="col">
              <div class="form-group">
              
              <label for="status">Etat :</label>  <select class="form-select" id="status">`;

  string += `<option value="${currentStates}">${currentStates}</option>`;
  statuses.forEach((state) => {
    if (state !== currentStates) {
      string += `<option value="${state}">${state}</option>`;
    }
  });
  string += `
          </select>
        </div>
      </div> 
    </div>`;

  string += `
      <div class="row">
          <div class="col">
            <div class="form-group"> 
              <label for="type">Type :</label> 
                <select class="form-select" id="types">
                  <option value="${currentType}">${currentType}</option>`;
  types.forEach((type) => {
    if (type !== currentType) {
      string += `<option value="${type}">${type}</option>`;
    }
  });
  string += `
          </select>
        </div>
      </div> 
    </div>`;

  const dateString = currentAvaibality === all ? all : currentAvaibality;

  string += `<div class="row">
        <div class="col">
            <div class="form-group"> 
          <label for="avaibality">Arrivée au parc :</label>   
          <select class="form-select" id="avaibality"><option value="${
            dateString === all ? all : dateString
          }">
              ${dateString === all ? all : formatteDate(dateString)}</option>`;
  availabilities.forEach((av) => {
    if (av !== currentAvaibality) {
      const strDate = av !== all ? transformDate(av) : all;
      string += `<option value="${strDate === all ? all : strDate}">${
        strDate === all ? all : new Date(strDate).toLocaleDateString()
      }</option>`;
    }
  });
  string += `
                          </select>
                      </div> 
                    </div>

                    <div class="row">
                        <div class="col">
                            <div class="form-group">
                                <label class="col-form-label mt-2" for="price-from">Prix minimum:</label>
                                <input type="number" class="form-control" id="price-from" step="0.01" min="0" max="10" value="${
                                  currentMin !== undefined ? currentMin : ''
                                }">
                            </div>
                        </div>

                        <div class="col">
                            <div class="form-group">
                                <label class="col-form-label mt-2" for="price-to">Prix maximum:</label>
                                <input type="number" class="form-control" id="price-to" value="${
                                  currentMin !== undefined ? currentMin : ''
                                }">
                            </div>
                        </div>
                    </div>
          
                    <div class="row py-2">
                      <div class="col text-center">
                          <button class="btn btn-primary" id="search">Rechercher</button>
                      </div>
                    </div>
          
    </div>
  </div>`;

  return string;
}
function formatteDate(date) {
  const dateParts = date.split('-');
  const formattedDate = `${dateParts[2]}/${dateParts[1].padStart(2, '0')}/${dateParts[0]}`;
  return formattedDate;
}
function transformDate(av) {
  const year = av[0];
  const month = av[1];
  const day = av[2];
  const date = `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`; // AAAA/MM/JJ
  return date;
}
function attachDataListListener() {
  const btnSearch = document.querySelector('#search');
  const state = document.querySelector('#status');
  const type = document.querySelector('#types');
  const avaibality = document.querySelector('#avaibality');
  const minPriceInput = document.querySelector(`#price-from`);
  const maxPriceInput = document.querySelector(`#price-to`);

  btnSearch.addEventListener('click', () => {
    currentAvaibality = avaibality.value;

    currentStates = state.value;
    currentType = type.value;
    console.log({ currentStates });
    console.log({ currentType });
    console.log({ currentAvaibality });
    if (minPriceInput.value !== '' && maxPriceInput.value !== '') {
      // Les deux inputs ont une valeur
      const minPrice = parseFloat(minPriceInput.value);

      const maxPrice = parseFloat(maxPriceInput.value);
      // faire quelque chose avec les valeurs entrées
      currentMin = minPrice === 0 ? undefined : minPrice;
      currentMax = maxPrice === 0 ? undefined : maxPrice;
    }
    getAllItem();
  });
}

async function getAllItem() {
  const itemsFromAPI = await getAllItemFromAPI(currentStates);

  const itemAsString = await getAllItemAsString(itemsFromAPI);

  const itemsWrapper = document.querySelector('#itemsWrapper');
  itemsWrapper.innerHTML = itemAsString;
  attachBtnListener();
}

async function getAllItemAsString(items) {
  const user = await getAuthenticatedFromApi();

  let itemAsString = ``;
  if (items.length === 0) {
    itemAsString += `<div class="container">
    <h1 class="text-center display-1">Aucun objet pour votre recherche </h1>
  </div>
  `;
  }
  // eslint-disable-next-line no-restricted-syntax
  for (const item of items) {
    // eslint-disable-next-line no-await-in-loop
    const pictureUri = await getPictureFromAPI(item.picture);

    itemAsString += `
    <div class="row py-3" id= "item${item.id}">
                        
      <div class="col">
          <div class="container py-3 border border-2 w-75 border-primary rounded rounded-3 ">
              <div class="row border-bottom border-primary">
                  <div class="col-3 border-end border-primary">
                      <img src="${pictureUri}" width="100%"  alt="...">
                  </div>
      
                  <div class="col my-auto">
                      <div class="container-fluid">
      
                      ${user.manager ? `<div class="row">
                      <div class="col">
                          <h4>Proposé par: ${
                            item.phoneNumber
                              ? `${item.phoneNumber}`
                              : `<a href="/userInformation?id=${item.idOferringUser}"class="text-decoration-none" title="Voir informations de l'utilisateur">
                              ${item.offeringUser}</a>`
                          }</h4>
                      </div>
                  </div>`: ``}
                          

                          <div class="row">
                              <div class="col">
                                  <h4>État: ${item.state} </h4>
                              </div>
      
                              <div class="col">
                                  <h4>Type: ${item.itemType}</h4>
                              </div>

                              ${
                                item.sellingPrice
                                  ? `
                                <div class="col">
                                  <h4>Prix:${item.sellingPrice} €</h4>
                                </div>`
                                  : ''
                              }
        
                          </div>
      
                          <div class="row ">
                              <div class="col-3">
                                  <h4>Description: </h4>
                              </div>
      
                              <div class="col">
                                  <h4>${item.description}</h4>
                              </div>
                          </div>

                          <div class="row">
                              <div class="col">
                                  <h4>Date d'arrivée au parc: ${new Date(
                                    item.dateParkArrival,
                                  ).toLocaleDateString()}</h4>
                              </div>

                              <div class="col">
                                  <h4>Plage horaire: ${item.timeSlot}</h4>
                              </div>
                          </div>
      
                          <div class="row py-1">
                            <div class="col text-center">
                            <a href="/item?id=${item.id}" class="btn btn-info">Voir information objet</a>
                          </div>
                      </div>
                    </div>
                  </div>
              
              
             
                  </div>
          <div class="row py-2">
              ${  item.state !=='vendu' ? `<h5>Action possible à effectuer:</h5>`: `` }
          
              <div class="container-fluid">
                  <div class="row">`;

          if (item.state === 'proposé' &&  user.manager){
            itemAsString+=`
            <div class="col-8 border-end border-primary ">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col">
                            <div class="form-group">
                                <label for="exampleTextarea" class="form-label mt-4">Raison du refus:</label>
                                <textarea class="form-control" id="reasonForRefusal${item.id}" rows="3"></textarea>
                            </div>
                        </div>
                    </div>

                    <div class="row  justify-content-end ">
                        <div class="col-5 my-1">
                            <button class="btn btn-danger btnDeclineProposal" data-id="${item.id}">Refuser la proposition</button>
                        </div>
                    </div>
                </div>
            </div>
        
            <div class="col-4 align-self-end">
                <button class="btn btn-primary btnAcceptProposal" data-id="${item.id}">Accepter la proposition</button>
            
            </div>`;
          }else if (item.state === 'accepté' || item.state === `à l'atelier`){
            itemAsString+=`
            
            <div class="row">
              <div class="col-4 ">
                  <button class="btn btn-primary btnIndicateItemStore" data-id="${item.id}">Indiquer en magasin</button>
              </div>`; 

            if (item.state !== `à l'atelier`){
              itemAsString += ` <div class="col-3">
              <button class="btn btn-primary btnindicateItemWorkshop" data-id="${item.id}">Indiquer en atelier</button>
          </div>`
            }

            itemAsString+= `</div>`;


          }

          if (item.state === `en magasin`) {
            itemAsString += `
            
            <div class="row">

            <div class="col-2 ">
                <div class="form-group">
                    <label class="col-form-label mt-4" for="price${item.id}">Prix:</label>
                    <input type="number" class="form-control" step="0.01" min="0" id="price${item.id}" name="price${item.id}" placeholder="Prix">
                    
                </div>
            </div>
            
            <div class="col-3  align-self-end">
                <button class="btn btn-primary btnindicateItemForSale" data-id="${item.id}">Mettre en vente</button>
            </div>`;
            if (user.manager) {
              itemAsString += `
              <div class="col-4  align-self-end">
                <button class="btn btn-primary btnindicateItemInShopForIsSold" data-id="${item.id}">Indiquer en tant que vendu</button>
              </div>`;
            }
            itemAsString+=`</div>`
          }

          if (item.state === `en vente`) {
            itemAsString += `
            <div class="row">
              <div class="col">
                  <button class="btn btn-primary btnindicateItemIsSold" data-id="${item.id}">Indiquer en tant que vendu</button>
              </div>
            </div>`;
          }

          itemAsString+= `</div> </div></div></div> </div> </div>
          ` 
  }

  return itemAsString;
}
async function getAllItemFromAPI(state) {
  let urlAPI = `api/items?state=`;
  if (currentStates !== all) {
    urlAPI += `${state}`;
  } else {
    urlAPI += `all`;
  }
  if (currentType !== all) {
    urlAPI += `&type=${currentType}`;
  }
  if (currentAvaibality !== all) {
    urlAPI += `&date=${currentAvaibality}`;
  }
  if (currentMin !== undefined && currentMax !== undefined) {
    urlAPI += `&amountMin=${currentMin}&amountMax=${currentMax}`;
  }

  const response = await fetch(urlAPI);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const allUsers = await response.json();
  return allUsers;
}

function deleteRowItem(idItem) {

   const rowItem = document.getElementById(`item${idItem}`);
   rowItem.remove();
}
function attachBtnListener() {
  const allBtnAcceptProposal = document.querySelectorAll('.btnAcceptProposal');
  const allBtnDeclineProposal = document.querySelectorAll('.btnDeclineProposal');
  const allbtnIndicateItemStore = document.querySelectorAll('.btnIndicateItemStore');
  const allbtnindicateItemWorkshop = document.querySelectorAll('.btnindicateItemWorkshop');
  const allbtnindicateItemForSale = document.querySelectorAll('.btnindicateItemForSale');
  const allbtnindicateItemIsSold = document.querySelectorAll('.btnindicateItemIsSold');
  const allbtnindicateItemInShopIsSold = document.querySelectorAll(
    '.btnindicateItemInShopForIsSold',
  );

  allBtnAcceptProposal.forEach((btn) => {
    btn.addEventListener('click', async (e) => {
      const btnItemClicked = e.target;
      const idItem = btnItemClicked?.dataset?.id;
      // eslint-disable-next-line no-console
      console.log({ idItem });
      const item = await acceptProposal(idItem);
      // eslint-disable-next-line no-console
      console.log({ item });
      deleteRowItem(idItem);
    });
  });
  allbtnindicateItemIsSold.forEach((btn) => {
    btn.addEventListener('click', async (e) => {
      const btnItemClicked = e.target;
      const idItem = btnItemClicked?.dataset?.id;
      // eslint-disable-next-line no-console
      console.log({ idItem });
      const item = await indicateItemIsSold(idItem);
      // eslint-disable-next-line no-console
      console.log({ item });
      deleteRowItem(idItem);
    });
  });
  allBtnDeclineProposal.forEach((btn) => {
    btn.addEventListener('click', async (e) => {
      const btnItemClicked = e.target;
      const idItem = btnItemClicked?.dataset?.id;
      // eslint-disable-next-line no-console
      console.log({ idItem });
      const textarea = document.querySelector(`#reasonForRefusal${idItem}`);
      const reasonForRefusal = textarea.value;

      const item = await declineProposal(idItem, reasonForRefusal);
      // eslint-disable-next-line no-console
      console.log({ item });
      deleteRowItem(idItem);
    });
  });

  allbtnIndicateItemStore.forEach((btn) => {
    btn.addEventListener('click', async (e) => {
      const btnItemClicked = e.target;
      const idItem = btnItemClicked?.dataset?.id;
      // eslint-disable-next-line no-console
      console.log({ idItem });

      const item = await indicateToStore(idItem);
      // eslint-disable-next-line no-console
      console.log({ item });
      deleteRowItem(idItem);
    });
  });
  allbtnindicateItemWorkshop.forEach((btn) => {
    btn.addEventListener('click', async (e) => {
      const btnItemClicked = e.target;
      const idItem = btnItemClicked?.dataset?.id;
      // eslint-disable-next-line no-console
      console.log({ idItem });

      const item = await indicateToWorkshop(idItem);
      // eslint-disable-next-line no-console
      console.log({ item });
      deleteRowItem(idItem);
    });
  });

  allbtnindicateItemForSale.forEach((btn) => {
    btn.addEventListener('click', async (e) => {
      const btnItemClicked = e.target;
      const idItem = btnItemClicked?.dataset?.id;
      // eslint-disable-next-line no-console
      console.log({ idItem });
      const textarea = document.querySelector(`#price${idItem}`);
      const price = textarea.value;

      const item = await indicateForSale(idItem, price);
      // eslint-disable-next-line no-console
      console.log({ item });
      deleteRowItem(idItem);
    });
  });

  allbtnindicateItemInShopIsSold.forEach((btn) => {
    btn.addEventListener('click', async (e) => {
      const btnItemClicked = e.target;
      const idItem = btnItemClicked?.dataset?.id;
      // eslint-disable-next-line no-console
      console.log({ idItem });
      const textarea = document.querySelector(`#price${idItem}`);
      const price = textarea.value;
      await indicateItemInShopForSale(idItem, price);

      deleteRowItem(idItem);
    });
  });
}

async function indicateItemInShopForSale(idItem, price) {
  const options = {
    method: 'PATCH',
    body: JSON.stringify({
      price,
    }),
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`api/items/indicateItemInShopIsSold/${idItem}`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const itemUpdated = await response.json();
  return itemUpdated;
}

async function indicateForSale(idItem, price) {
  const options = {
    method: 'PATCH',
    body: JSON.stringify({
      price,
    }),
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`api/items/indicateItemForSale/${idItem}`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const itemUpdated = await response.json();
  return itemUpdated;
}
async function indicateToStore(idItem) {
  const options = {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`api/items/indicateItemStore/${idItem}`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const itemUpdated = await response.json();
  return itemUpdated;
}
async function indicateToWorkshop(idItem) {
  const options = {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`api/items/indicateItemWorkshop/${idItem}`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const itemUpdated = await response.json();
  return itemUpdated;
}
async function indicateItemIsSold(idItem) {
  const options = {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`api/items/indicateItemIsSold/${idItem}`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const itemUpdated = await response.json();
  return itemUpdated;
}

async function acceptProposal(idItem) {
  const options = {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`api/items/acceptProposal/${idItem}`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const itemUpdated = await response.json();
  return itemUpdated;
}
async function declineProposal(idItem, reasonForRefusal) {
  const options = {
    method: 'PATCH',
    body: JSON.stringify({
      reasonForRefusal,
    }),
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`api/items/declineProposal/${idItem}`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const itemUpdated = await response.json();
  return itemUpdated;
}

export default ItemsPage;
