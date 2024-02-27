import { clearPage } from '../../../utils/render';
import Navigate from '../../Router/Navigate';
import { getPictureFromAPI } from '../../../utils/fileUpload';
import { getAuthenticatedUser } from '../../../utils/auths';

const types = [
  'Meuble',
  'Table',
  'Chaise',
  'Fauteuil',
  'Lit/sommier',
  'Matelas',
  'Couvertures',
  'Matériel de cuisine',
  'Vaisselle',
];

const ItemEditPage = async () => {
  clearPage();
  const urlParams = new URLSearchParams(window.location.search);
  const id = urlParams.get('id');

  if (id === null) {
    Navigate('/');
  } else {
    await renderItemPage(id);
  }
};
async function renderItemPage(id) {
  const item = await getItemFromAPI(id);

  const main = document.querySelector('main');

  const pictureUri = await getPictureFromAPI(item.picture);

  let datalist = `<select class="form-select" id="types"><option value="${item.itemType}">${item.itemType}</option>`;
  types.forEach((type) => {
    if (type !== item.itemType) {
      datalist += `<option value="${type}">${type}</option>`;
    }
  });
  datalist += `</select>`;

  main.innerHTML += `

  <h1 class="text-center" style="margin-top: 120px;">Modifier un objet</h1>
  <p id="id" hidden>${item.id}<p>
    <div class="container py-3 border border-2 w-75 border-primary rounded rounded-5 shadow-lg mb-5">
        <div class="row">
            <div class="col-3 border-end border-primary">
                <img src="${pictureUri}" width="100%"  alt="...">
            </div>

            <div class="col my-auto">
                <div class="container-fluid">

                    <div class="row">
                        <div class="col">
                            <h4><u>Informations sur l'objet:</u></h4>
                        </div>
                    </div>

                    <div class="row">

                        <div class="col align-self-end">
                            <h4>État: ${item.state}</h4>
                        </div>

                        <div class="col my-auto ">
                            <div class="form-group">
                                <label for="itemType" class="form-label mt-4">Type:</label>
                                ${datalist}
                            </div>
                        </div>
                        ${
                          item.sellingPrice
                            ? ` <div class="col ">
                            <div class="form-group">
                                <label class="col-form-label mt-4" for="sellingPrice">Prix de vente:</label>
                                <input type="number" class="form-control" id="sellingPrice" value="${item.sellingPrice}">
                            </div>
                        </div>`
                            : ''
                        }

                        

                    </div>

                    <div class="row ">
                        <div class="col">
                            <div class="form-group">
                                <label for="description" class="form-label mt-4">Description:</label>
                                <textarea class="form-control" class="form-control" id="description"  rows="2">${item.description}</textarea>
                            </div>
                        </div>
                    </div>
          

                    <div class="row ">
                        <div class="col-9  ">
                            <div class="form-group">
                                <label for="formFile" class="form-label mt-4">Sélectionnez la nouvelle photo:</label>
                                 <input class="form-control" name="file" type="file" id="formFile">
                            </div>
                        </div>
                      

                        <div class="col  align-self-end">

                            <button class="btn btn-primary" id ="btnUpdatePicture">Changer la photo</button>
                        </div>
                    </div>

                    <div class="row py-2">
                        <div class="col text-center">  
                            <button type="button" id ="btnEdit" class="btn btn-info">Mettre à jour 'lobjet</button>
                        </div>
                    </div>
                    <div id="error-container"></div>

                </div>
            </div>
        </div>
    </div>
      `;

  const btn = document.querySelector('#btnEdit');

  btn.addEventListener('click', updateItem);

  const btnPicture = document.querySelector('#btnUpdatePicture');

  btnPicture.addEventListener('click', updatePicture);
}

async function updatePicture() {
  const id = document.querySelector('#id').textContent;

  const fileInput = document.querySelector('input[name=file]');
  const formData = new FormData();
  formData.append('file', fileInput.files[0]);
  const options = {
    method: 'PATCH',
    body: formData,
  };
  const response = await fetch(`api/updatePictureItem/${id}`, options);

  if (!response.ok) {
    const errorContainer = document.getElementById('error-container');
    const error = await response.text();
    errorContainer.innerHTML = `<p class="alert alert-danger">${error}</p>`;
    throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  }else{
    Navigate(`editItem?id=${id}`);
  }
}
async function updateItem(e) {
  e.preventDefault();

  const itemType = document.querySelector('#types').value;
  const id = document.querySelector('#id').textContent;
  // eslint-disable-next-line no-console
  console.log({ id });
  const description = document.querySelector('#description').value;
  const sellingPrice = document.querySelector('#sellingPrice')?.value;

  const body = {
    id,
    itemType,
    description,
  };

  if (sellingPrice !== undefined) {
    body.sellingPrice = sellingPrice;
  }

  const options = {
    method: 'PATCH',
    body: JSON.stringify(body),
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`api/items`, options);

  if (!response.ok) {
    const errorContainer = document.getElementById('error-container');
    const error = await response.text();
    errorContainer.innerHTML = `<p class="alert alert-danger">${error}</p>`;
    throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  }

  Navigate(`item?id=${id}`);
}

async function getItemFromAPI(id) {
  const response = await fetch(`api/items/${id}`);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const item = await response.json();
  return item;
}

export default ItemEditPage;
