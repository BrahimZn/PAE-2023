/* eslint-disable no-console */
// eslint-disable-next-line import/no-extraneous-dependencies, import/no-unresolved
import Glide from '@glidejs/glide'
import Navbar from '../Navbar/Navbar';
import { getPictureFromAPI } from '../../utils/fileUpload';
import { clearPage } from '../../utils/render';

let types = [];
const typeAll = 'Tout';
let currentType = typeAll;
const HomePage = async () => {
  clearPage();
  Navbar();
  const typesFromApi = await getAllItemTypeFromAPI();
  const typesLabel = typesFromApi.map((t) => t.label);
  types = [];
  types = typesLabel;
  types.push(typeAll);
  attachDataList();
  await renderPage();
  attachListener();
};


async function renderPage() {
  const itemsFromAPI = await getAllItemFromAPI();

  const itemAsString = await getAllItemAsString(itemsFromAPI);

  const stringHtml = `
<script src="https://cdn.jsdelivr.net/npm/@glidejs/glide"></script>
  <h1 class="text-center py-2" style="margin-top: 100px;">Accueil</h1>
  <div class="container-fluid ">

      <div class="row">
          
          <div class="col-2 position-fixed">
              <div class="container-fluid">
                  <div class="row">
                      <div class="col">
                          <div class="form-group">
                              <label for="types" class="form-label mt-4">Filtrez par type d'objets:</label>
                              ${attachDataList()}
                          </div>
                      </div>
                  </div>
                  
              </div>
          </div>


          <div class="col " style="padding-left: 17%;"> 
                <div class="container border border-2 rounded rounded-5 border-primary p-3">

                    <div class="glide">
                        <div class="glide__track" data-glide-el="track">
                            <ul class="glide__slides" id="itemsWrapper" >

                    ${itemAsString}
                  </ul>
                       </div> 
                       <div class="glide__arrows text-center" id="btnGlide" data-glide-el="controls">
                            <button  class="glide__arrow glide__arrow--left btn btn-info" data-glide-dir="<">Précedeent</button>
                            <button  class="glide__arrow glide__arrow--right btn btn-info" data-glide-dir=">">Suivant</button>
                        </div>
                    </div>
                </div>
            </div>
          
    </div>
  </div>   `;
  const main = document.querySelector(`main`);

  main.innerHTML = stringHtml;
  await renderCarousel(itemsFromAPI);
}
async function getAllItemTypeFromAPI() {
  const response = await fetch(`api/items/types`);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const allUsers = await response.json();
  return allUsers;
}

function attachDataList() {
  let datalist = `<select class="form-select" id="types"><option value="${currentType}">${currentType}</option>`;
  types.forEach((type) => {
    if (type !== currentType) {
      datalist += `<option value="${type}">${type}</option>`;
    }
  });
  datalist += `</select>`;

  return datalist;
}
function attachListener() {
  const select = document.querySelector('#types');
  const selectedValue1 = select.value;
  // eslint-disable-next-line no-console
  console.log({ selectedValue1 });
  select.addEventListener('change', () => {
    const selectedValue = select.value;
    currentType = selectedValue;
    // eslint-disable-next-line no-console
    console.log({ currentType });
    getAllItem();
  });
}

async function getAllItem() {
  const itemsFromAPI = await getAllItemFromAPI();

  const itemAsString = await getAllItemAsString(itemsFromAPI);
  const itemWrapper = document.querySelector('#itemsWrapper');
  itemWrapper.innerHTML = itemAsString;
  await renderCarousel(itemsFromAPI);
}

async function renderCarousel(items) {
  const nbItem = items.length;

  const btnGlide = document.getElementById("btnGlide");

  if(btnGlide.classList.contains("d-none")) {
    btnGlide.classList.remove("d-none");
  }

  const carousel = new Glide('.glide', {
    type: 'carousel',
    startAt: 0,
    perView: 3,
  });

  if (nbItem < 3) {
    carousel.update({
      type: 'slider',
      controls: false,
      touchMove: false,
      swipeThreshold: false,
    });

    btnGlide.classList.add("d-none");
  }

  if(nbItem === 0) {
    carousel.update({
      focusAt: 'center',
    })
  }

  carousel.mount();
}

async function getAllItemAsString(items) {
  let itemAsString = ``;

  if (items.length === 0) {
    itemAsString += `<li class="glide__slide">
    <h5 class="text-center display-1">Aucun objet de type "${currentType}"</h5>
  </li>
  
  `;
  }

  // eslint-disable-next-line no-restricted-syntax
  for (const item of items) {
    // eslint-disable-next-line no-await-in-loop
    const pictureUri = await getPictureFromAPI(item.picture);

    itemAsString += `<li class="glide__slide">
    <div class="card text-white bg-primary mb-3 mx-3" style="max-width: 22rem;">
      <img src="${pictureUri}" class="card-img-top" alt="...">
      <div class="card-body">
        <h4 class="card-title text-center">Etat:  ${item.state} </h4>
        <h4 class="card-title text-center">Type: ${item.itemType}</h4>
        ${
          item.sellingPrice
            ? `<h4 class="card-text text-center">Prix: ${item.sellingPrice} €</h4>`
            : ''
        } 
        <div class="text-center">
            <h3><a href="/item?id=${item.id}" class="card-text btn btn-warning">Voir plus d'informations</a></h3>
        </div>
      </div>
    </div>
  </li>
    `;
  }

  return itemAsString;
}
async function getAllItemFromAPI() {
  let urlAPI = `api/items?state=visible`;
  if (currentType !== typeAll) {
    urlAPI += `&type=${currentType}`;
  }
  const response = await fetch(urlAPI);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
  const allUsers = await response.json();
  return allUsers;
}

export default HomePage;
