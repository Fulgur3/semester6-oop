import './App.css';
import {
	Route,
	NavLink,
	HashRouter
} from "react-router-dom";
import Login from "./components/login.jsx";
import Register from "./components/register.jsx";
import Cars from "./components/cars.jsx";
import CarInfo from "./components/car-info.jsx";
import Request from "./components/new-request.jsx";
import RequestsAdmin from "./components/admin.jsx";
import MyStatus from "./components/main.jsx";
import AdminStats from "./components/stats.jsx";
import NewCar from "./components/new-car.jsx";

function App() {
  return (
	  <HashRouter>

 <div class="App">
	  <img id="banner-img" src="https://www.adelaideairport.com.au/wp-content/uploads/2014/12/parking.jpg"/>

          <h1>Car Rental</h1>
          <ul className="header">
	    <li><NavLink exact to="/">Main</NavLink></li>
	    <li><NavLink to="/cars">Cars</NavLink></li>
            <li><NavLink to="/login">Change user</NavLink></li>
	    {localStorage.getItem('isAdmin') === 'true' && <li><NavLink to="/admin">Admin</NavLink></li>}
	    {localStorage.getItem('isAdmin') === 'true' && <li><NavLink to="/stats">Stats</NavLink></li>}
          </ul>

          <div className="content">
            <Route path="/login" component={Login}/>
            <Route path="/register" component={Register}/>
            <Route path="/car" component={CarInfo}/>
            <Route path="/request" component={Request}/>
	    <Route path="/admin" component={RequestsAdmin}/>
            <Route path="/cars" component={Cars}/>
            <Route path="/stats" component={AdminStats}/>
            <Route path="/new-car" component={NewCar}/>
            <Route exact path="/" component={MyStatus}/>
          </div>
	  
	  <div class="footer">
	    <span>Білокриницький Олександр</span>
	    <span>ІПС-31</span>
	    <span>(098) 322 22 11</span>
	  </div>
        </div>


	  </HashRouter>
  );
}

export default App;
