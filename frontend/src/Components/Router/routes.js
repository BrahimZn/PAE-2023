import HomePage from '../Pages/HomePage';
import ConnexionPage from '../Pages/auth/ConnexionPage';
import Logout from '../Logout/Logout';
import AllUsersPage from '../Pages/Manager/AllUsersPage';
import RegisterPage from '../Pages/auth/RegisterPage';
import ItemsPage from '../Pages/Helper/ItemsPage';
import PropositionPage from '../Pages/items/PropositionPage';
import ProfilePage from '../Pages/Profile/ProfilePage';
import EditProfilePage from '../Pages/Profile/EditProfilePage';
import ListItemsForUser from '../Pages/members/MemberItems';

// eslint-disable-next-line import/no-unresolved
import AddAvailabilityPage from '../Pages/Helper/addAvailabilityPage';
import ItemInfoPage from '../Pages/items/ItemInfoPage';
import ItemsRemovablePage from '../Pages/Helper/ItemsRemovable';
import ItemEditPage from '../Pages/items/ItemUpdatePage';
import NotificationPage from '../Pages/Helper/NotificationPage';
import UserInformationPage from '../Pages/members/UserInformationPage';
import DashboardPage from '../Pages/Helper/DashboardItems';

const routes = {
  '/': HomePage,
  '/connexion': ConnexionPage,
  '/register': RegisterPage,
  '/logout': Logout,
  '/allUsers': AllUsersPage,
  '/itemsManagement': ItemsPage,
  '/propositionItem': PropositionPage,
  '/profile': ProfilePage,
  '/editProfile': EditProfilePage,
  '/addAvailability': AddAvailabilityPage,
  '/item': ItemInfoPage,
  '/itemsRemovable': ItemsRemovablePage,
  '/listItems': ListItemsForUser,
  '/editItem': ItemEditPage,
  '/notifications': NotificationPage,
  '/userInformation': UserInformationPage,
  '/tableauDeBord': DashboardPage,
};

export default routes;
