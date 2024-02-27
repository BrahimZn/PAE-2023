/* eslint-disable no-console */
import { getAuthenticatedUser } from '../../../utils/auths';
import { clearPage } from '../../../utils/render';

const AddAvailabilityPage = async () => {
  clearPage();
  renderForm();
};

function renderForm() {
  const str = `
  <h1 class="text-center" style="margin-top: 150px;">Ajouts de disponibilités</h1>
    <div class="container py-3 border border-2 w-50 border-primary rounded rounded-5 shadow-lg mb-5">

          <div id="error-container"></div>
        
        <form id="availability-form">
          <div class="row">
              <div class="col">
                  <div class="form-group">
                      <label for="dateParkArrival">Date de venu au parc</label>
                      <input type="date" class="form-control" id="availability" name="availability" list="date-list">
                      <small id="dateHelp" class="form-text text-muted">Introduisez seulement un samedi auxquels un responsable ou un aidant est disponible.</small>
                  </div>
                  
              </div>
          </div>

          <div class="row py-3">
            <div class="col text-center">
              <button type="submit" class="btn btn-primary">Ajouter</button>
            </div>
        </div>
        </form>
    </div> 
    
   
  `;
  const main = document.querySelector('main');
  main.innerHTML += str;

  const form = document.getElementById('availability-form');

  form.addEventListener('submit', addAvailability);
}
async function addAvailability(e) {
  e.preventDefault();
  const errorContainer = document.getElementById('error-container');
  const availability = document.getElementById('availability').value;
  const options = {
    method: 'POST',
    body: JSON.stringify({
      availability,
    }),
    headers: {
      'Content-Type': 'application/json',
      authorization: getAuthenticatedUser().token,
    },
  };

  const response = await fetch('api/items/availabilities', options);

  if (!response.ok) {
    const error = await response.text();
    errorContainer.innerHTML=`<div class="row">
    <div class="col">
        <div class="alert alert-dismissible alert-danger">
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            ${error}
        </div>
    </div>
</div>`
    throw new Error(error);
  } else {
    errorContainer.innerHTML = `<div class="row">
    <div class="col">
        <div class="alert alert-dismissible alert-success">
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
             La date de disponibilité a été ajoutée.
        </div>
    </div>
</div>`;
  }

}

export default AddAvailabilityPage;
