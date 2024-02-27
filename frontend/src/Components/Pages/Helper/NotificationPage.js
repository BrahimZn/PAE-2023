import Navbar from '../../Navbar/Navbar';
import {clearPage} from '../../../utils/render';

const NotificationPage = async () => {
    Navbar();
    clearPage();
    await NotifNotReadConstructor();
    await NotifReadConstructor();
    await addEventListener()
  };


  async function addEventListener(){
    const allNotifications = await getNotificationNotRead();
    allNotifications.forEach((notification) => {
      const qs = `[name="${notification.itemId}"]`;
      document.querySelector(qs).addEventListener('click', () => {
        read(notification.notifId);
      });
    });
  }


  async function NotifNotReadConstructor(){
    const allNotifications = await getNotificationNotRead();

    document.querySelector('main').innerHTML += `<h1 class="text-center" style="margin-top: 120px;">Notifications non-lues</h1>`;

    if(allNotifications.length === 0){
      document.querySelector('main').innerHTML += `<h6 class="text-center">Vous n'avez pas de notifications non-lues</h6>`;

    }else{
    allNotifications.forEach(async (notification) => {
      const notificationString = `<div class="card mx-auto my-3" style="width: 35rem;">
        <div class="card-body">
            <h5 class="card-title">${notification.notificationText}</h5>
            <p class="card-text">Objet à l'id : ${notification.itemId}</p>
            <button class="btn btn-primary" name=${notification.itemId}>Marqué comme lu</button>
            <a href="/item?id=${notification.itemId}" class="btn btn-primary">Aller à l'objet</a>
        </div>
      </div>`;
      document.querySelector('main').innerHTML += notificationString;
    });
  }
}


  async function NotifReadConstructor(){
    const allNotif = await getAllNotification();
    const notReadNotif = await getNotificationNotRead();
    const readNotif = allNotif.filter(notification => !notReadNotif.some(notifications => notifications.notifId === notification.notifId));

    document.querySelector('main').innerHTML += `<h1 class="text-center">Notifications lues</h1>`;

    if(readNotif.length === 0){
      document.querySelector('main').innerHTML += `<h6 class="text-center">Vous n'avez pas de notifications lues</h6>`;

    }else{
    readNotif.forEach(async (notification) => {
      const notificationString = `<div class="card mx-auto my-3" style="width: 35rem;">
        <div class="card-body">
            <h5 class="card-title">${notification.notificationText}</h5>
            <p class="card-text">Objet à l'id : ${notification.itemId}</p>
            <button class="btn btn-primary" name=${notification.itemId}>Lu</button>
            <a href="/item?id=${notification.itemId}" class="btn btn-primary">Aller à l'objet</a>
        </div>
      </div>`;
      document.querySelector('main').innerHTML += notificationString;
    });
  }
}


  async function getNotificationNotRead() {
    const {token} = JSON.parse(sessionStorage.getItem("user"));

    const response = await fetch(`api/notifications/my`, {
      method: 'GET',
      headers: {
        'Authorization': token
      }
    });
    
    if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
    return response.json();
  }


  async function getAllNotification() {
    const {token} = JSON.parse(sessionStorage.getItem("user"));

    const response = await fetch(`api/notifications`, {
      method: 'GET',
      headers: {
        'Authorization': token
      }
    });
    
    if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);
    return response.json();
  }


  async function read(idN){
    
    const {token} = JSON.parse(sessionStorage.getItem("user"));

    const response = await fetch("api/notifications/read", {
    method: 'POST',
    body: JSON.stringify({
      id: idN}),
    headers: {
        "Content-type": "application/json",
        "Authorization": token  
    }});

    if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);

    // eslint-disable-next-line no-restricted-globals
    location.reload();

  }
  

export default NotificationPage;
