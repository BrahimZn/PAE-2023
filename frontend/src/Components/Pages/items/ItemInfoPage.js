import { clearPage } from '../../../utils/render';
import Navigate from '../../Router/Navigate';
import { getPictureFromAPI } from '../../../utils/fileUpload';
import {
  getAuthenticatedFromApi,
  isAuthenticated,
  getAuthenticatedUser,
} from '../../../utils/auths';

const ItemInfoPage = async () => {
  clearPage();
  const urlParams = new URLSearchParams(window.location.search);
  const id = urlParams.get('id');

  if (id === null) {
    Navigate('/');
  } else {
    await renderItemPage(id);
    attachBtnListener();
  }
};

async function renderItemPage(id) {
  const item = await getItemFromAPI(id);

  let user;
  if (isAuthenticated()) {
    user = await getAuthenticatedFromApi();
  }

  const main = document.querySelector('main');

  const pictureUri = await getPictureFromAPI(item.picture);

  let itemAsString = `
  <h1 class="text-center" style="margin-top: 120px;">Informations sur l'objet</h1> <div class="container py-3 border border-2 w-75 border-primary rounded rounded-3 ">
  <div class="row border-bottom border-primary">
      <div class="col-3 border-end border-primary">
          <img src="${pictureUri}" width="100%"  alt="...">
      </div>

      <div class="col my-auto">
          <div class="container-fluid">
          ${user?.manager  ?`
              <div class="row">
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

              ${
                item.reasonForRefusal
                  ? `<div class="row">
                  <div class="col">
                      <h4>Raison du refus: ${
                        item.reasonForRefusal
                      }</h4>
                  </div>
              </div>`
                  : ''
              }
              <div class="row py-1">
                <div class="col text-center">
                ${
                  user !== undefined && user.manager
                    ? `<a href="/editItem?id=${item.id}" class="btn btn-info">Modifier objet</a>`
                    : ''
                }
              </div>
          </div>
        </div>
      </div>
  
  
 
      </div>
<div class="row py-2">
  ${
    item.state !== 'vendu' && item.state !== 'refusé' && user !== undefined
      ? `<h5>Action possible à effectuer:</h5>`
      : ``
  }

  <div class="container-fluid">
      <div class="row">`;
  if (user !== undefined) {
    if (item.state === 'proposé' && user.manager) {
      itemAsString += `
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
                <button class="btn btn-danger btnDeclineProposal" id="btnDeclineProposal" data-id="${item.id}">Refuser la proposition</button>
            </div>
        </div>
    </div>
</div>

<div class="col-4 align-self-end">
    <button class="btn btn-primary btnAcceptProposal" id = "btnAcceptProposal" data-id="${item.id}">Accepter la proposition</button>

</div>`;
    } else if (item.state === 'accepté' || item.state === `à l'atelier`) {
      itemAsString += `

<div class="row">
  <div class="col-4 ">
      <button class="btn btn-primary btnIndicateItemStore" id="btnIndicateItemStore" data-id="${item.id}">Indiquer en magasin</button>
  </div>`;

      if (item.state !== `à l'atelier`) {
        itemAsString += ` <div class="col-3">
  <button class="btn btn-primary btnindicateItemWorkshop" id="btnindicateItemWorkshop" data-id="${item.id}">Indiquer en atelier</button>
</div>`;
      }

      itemAsString += `</div>`;
    }

      if (item.state === `en magasin`) {

        const etat = item.state
        // eslint-disable-next-line no-console
        console.log({etat});
        itemAsString += `

<div class="row">

<div class="col-2 ">
    <div class="form-group">
        <label class="col-form-label mt-4" for="price${item.id}">Prix:</label>
        <input type="number" class="form-control" step="0.01" min="0" id="price${item.id}" name="price${item.id}" placeholder="Prix">
        
    </div>
</div>

<div class="col-3  align-self-end">
    <button class="btn btn-primary btnindicateItemForSale" id = "btnindicateItemForSale" data-id="${item.id}">Mettre en vente</button>
</div>`;
        if (user.manager) {
          itemAsString += `
  <div class="col-4  align-self-end">
    <button class="btn btn-primary btnindicateItemInShopForIsSold" id="btnindicateItemInShopForIsSold" data-id="${item.id}">Indiquer en tant que vendu</button>
  </div>`;
        }
        itemAsString += `</div>`;
      }

      if (item.state === `en vente`) {
        itemAsString += `
<div class="row">
  <div class="col">
      <button class="btn btn-primary btnindicateItemIsSold" id="btnindicateItemIsSold" data-id="${item.id}">Indiquer en tant que vendu</button>
  </div>
</div>`;
      }
    }
  

  itemAsString += `</div> </div>`;

  main.innerHTML += itemAsString;
}

async function getItemFromAPI(id) {
  const response = await fetch(`api/items/${id}`);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const item = await response.json();
  return item;
}

function attachBtnListener() {
  const btnAcceptProposal = document.querySelector('#btnAcceptProposal');
  const btnDeclineProposal = document.querySelector('#btnDeclineProposal');
  const btnIndicateItemStore = document.querySelector('#btnIndicateItemStore');
  const btnindicateItemWorkshop = document.querySelector('#btnindicateItemWorkshop');
  const btnindicateItemForSale = document.querySelector('#btnindicateItemForSale');
  const btnindicateItemIsSold = document.querySelector('#btnindicateItemIsSold');
  const btnindicateItemInShopIsSold = document.querySelector('#btnindicateItemInShopForIsSold');

  btnAcceptProposal?.addEventListener('click', async (e) => {
    const btnItemClicked = e.target;
    const idItem = btnItemClicked?.dataset?.id;
    await acceptProposal(idItem);
    Navigate(`item?id=${idItem}`)
  });

  btnindicateItemIsSold?.addEventListener('click', async (e) => {
    const btnItemClicked = e.target;
    const idItem = btnItemClicked?.dataset?.id;

    await indicateItemIsSold(idItem);
    Navigate(`item?id=${idItem}`)
  });

  btnDeclineProposal?.addEventListener('click', async (e) => {
    const btnItemClicked = e.target;
    const idItem = btnItemClicked?.dataset?.id;
    // eslint-disable-next-line no-console
    console.log({ idItem });
    const textarea = document.querySelector(`#reasonForRefusal${idItem}`);
    const reasonForRefusal = textarea.value;

    await declineProposal(idItem, reasonForRefusal);
    Navigate(`item?id=${idItem}`)
  });

  btnIndicateItemStore?.addEventListener('click', async (e) => {
    const btnItemClicked = e.target;
    const idItem = btnItemClicked?.dataset?.id;

    await indicateToStore(idItem);
    Navigate(`item?id=${idItem}`)
  });

  btnindicateItemWorkshop?.addEventListener('click', async (e) => {
    const btnItemClicked = e.target;
    const idItem = btnItemClicked?.dataset?.id;
    // eslint-disable-next-line no-console
    console.log({ idItem });

    await indicateToWorkshop(idItem);
    Navigate(`item?id=${idItem}`)
  });

  btnindicateItemForSale?.addEventListener('click', async (e) => {
    const btnItemClicked = e.target;
    const idItem = btnItemClicked?.dataset?.id;
    // eslint-disable-next-line no-console
    console.log({ idItem });
    const textarea = document.querySelector(`#price${idItem}`);
    const price = textarea.value;

    await indicateForSale(idItem, price);
    Navigate(`item?id=${idItem}`)
  });

  btnindicateItemInShopIsSold?.addEventListener('click', async (e) => {
    const btnItemClicked = e.target;
    const idItem = btnItemClicked?.dataset?.id;
    // eslint-disable-next-line no-console
    console.log({ idItem });
    const textarea = document.querySelector(`#price${idItem}`);
    const price = textarea.value;
    await indicateItemInShopForSale(idItem, price);
    Navigate(`item?id=${idItem}`)
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

export default ItemInfoPage;
