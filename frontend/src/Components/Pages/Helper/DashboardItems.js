/* eslint-disable no-unused-vars */
/* eslint-disable no-alert */
// eslint-disable-next-line import/no-extraneous-dependencies
import Chart from 'chart.js/auto';
import Navbar from '../../Navbar/Navbar';
import { clearPage } from '../../../utils/render';
import { getAuthenticatedUser } from '../../../utils/auths';

const dashboardPage = async () => {
  clearPage();
  await Navbar();
  await getGraphic();
};

async function getGraphic() {
  const main = document.querySelector('main');
  main.innerHTML += `<h1 class="text-center" style="margin-top: 150px;">Tableau de bord</h1>
    <div class="container-fluid">
      <div class="row">
        <div
          class="col-2 position-fixed border rounded rounded-3 border-primary border-2 px-1"
        >
          <div class="container-fluid">
            <div class="row py-1">
              <div class="col text-center">
                <div class="form-group">
                  <label for="chartTypeSelect">Type de graphique :</label>
                  <select id="chartTypeSelect" name="chartType" class="form-select">
                    <option value="bar">Barre vertical</option>
                    <option value="pie">Tarte</option>
                    <option value="doughnut">Donut</option>
                    <option value="polarArea">Aires polaires</option>
                  </select>
                </div>
              </div>
            </div>
    
            <div class="row py-1">
              <div class="col text-center">
                <label for="startDateSelect">Date de début :</label>
                <input type="date" id="startDateSelect" class="form-select" value="${new Date().getFullYear()}-01-01" />
              </div>
            </div>
    
            <div class="row py-1">
              <div class="col text-center">
                <label for="endDateSelect">Date de fin :</label>
                <br>
                <input type="date" id="endDateSelect" class="form-select" value="${new Date().getFullYear()}-12-31" />
              </div>
            </div>
    
            <div class="row py-1">
              <div class="col text-center">
                <button id="updateButton" class="btn btn-primary">
                  Mettre à jour
                </button>
              </div>
            </div>
          </div>
        </div>
    
        <div class="col" style="padding-left: 10%">
          <div
            class="container border border-2 rounded rounded-5 border-primary p-3 w-75 bg-light"
          >
            <div class="row w-75 mx-auto">
              <div class="col w-75">
                <canvas id="myChart"></canvas>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    `;
  await updateChart();
  const updateButton = document.getElementById('updateButton');

  updateButton.addEventListener('click', updateChart);
}

let myChart = null;

async function updateChart() {
  const chartTypeSelect = document.getElementsByName('chartType')[0];
  const startDateSelect = document.getElementById('startDateSelect');
  const endDateSelect = document.getElementById('endDateSelect');

  const chartType = chartTypeSelect.value;
  const startDate = startDateSelect.value;
  const endDate = endDateSelect.value;

  const queryString = `?dateStart=${startDate}&dateEnd=${endDate}`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': getAuthenticatedUser().token,
    },
  };

  const response = await fetch(`/api/items/statistics${queryString}`, options);
  const data = await response.json();

  const keys = Object.keys(data);
  const values = Object.values(data);

  const chartData = {
    labels: keys,
    datasets: [
      {
        label: "nombre d'objets",
        backgroundColor: [
          'rgba(255, 99, 132, 0.2)',
          'rgba(54, 162, 235, 0.2)',
          'rgba(255, 206, 86, 0.2)',
          'rgba(75, 192, 192, 0.2)',
        ],
        borderColor: [
          'rgba(255, 99, 132, 1)',
          'rgba(54, 162, 235, 1)',
          'rgba(255, 206, 86, 1)',
          'rgba(75, 192, 192, 1)',
        ],
        borderWidth: 1,
        data: values,
      },
    ],
  };

  if (myChart) {
    myChart.destroy();
  }

  myChart = new Chart(document.getElementById('myChart'), {
    type: chartType,
    data: chartData,
  });
}

export default dashboardPage;
