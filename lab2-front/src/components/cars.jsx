import React, { Component } from "react";
import { fetchGetJson } from "../utils.js";
import API_SERVER from "../Constants.js";
import { carToComponent } from "./car.jsx";
import { NavLink } from "react-router-dom";

class Cars extends Component {
	constructor(props) {
		super(props);
		this.state = {
			manufacturer: '---',
			cars: [],
			manufacturers: ['---']
		}
		this.getCars = this.getCars.bind(this);
		this.onSelectManufacturer = this.onSelectManufacturer.bind(this);

		fetchGetJson(API_SERVER + "/car-manufacturers")
		.then((result) => {
			this.setState({
				manufacturers: ['---'].concat(result)
			})
		});
		this.getCars('---');
	}

	onSelectManufacturer(event) {
		this.setState({
			manufacturer: event.target.value
		});
		this.getCars(event.target.value);
	};

	getCars(manufacturer) {
		let apiMethod = "/cars-available";
		if (manufacturer !== '---') {
			apiMethod += "/" + manufacturer;
		}
		fetchGetJson(API_SERVER + apiMethod)
		.then((result) => {
			console.log(result);
			this.setState({
				cars: result
			});
		});
	}
	render() {
		return <div>
			{localStorage.getItem('isAdmin') === 'true' && <NavLink to="/new-car">Add new car</NavLink>}
			<br />
			<h2>Available cars:</h2>
			<label>
				Manufacturer:
				<select onChange={this.onSelectManufacturer} defaultValue='---'>
					{this.state.manufacturers.map((manufacturer) =>
						<option value={manufacturer}>{manufacturer}</option>)}
				</select>
			</label>
			<div class="cars">
				{this.state.cars.map(carToComponent)}
			</div>
		</div>
	}
}

export default Cars;
