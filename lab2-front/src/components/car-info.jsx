import React, { Component } from "react";
import { fetchPostJson, fetchGetJson, lastSegment } from "../utils.js";
import { NavLink } from "react-router-dom";
import API_SERVER from "../Constants.js";
import { paymentsToTotal, paymentToComponent } from "./payment.jsx";

class CarInfo extends Component {
	constructor(props) {
		super(props);
		const carId = lastSegment(window.location.href);
		this.state = {
			model: "",
			manufacturer: "",
			description: "",
			thumbnailUrl: "",
			uahPerDay: "",
			payments: []
		}
		fetchGetJson(API_SERVER + `/car/${carId}`)
		.then((result) => {
			console.log(result);
			this.setState(result);
		});
		if (localStorage.getItem('isAdmin') === 'true') {
			fetchGetJson(API_SERVER + `/payments/car/${carId}`)
			.then((result) => {
				this.setState({
					payments: result
				});
			});
		}
	}
	render() {
		const priceFormat = new Intl.NumberFormat('uk-UA', { style: 'currency', currency: 'UAH' });

		return <div class="car-info-page">
			<h2 id="manufacturer">{this.state.manufacturer}</h2>
			<h3 id="model">{this.state.model}</h3>
			<div class="img-desc">
				<img src={this.state.thumbnailUrl} />
				<p class="description">{this.state.description}</p>
			</div>
			<br />
			<span class="price">Price: <b>{priceFormat.format(this.state.uahPerDay)}</b> per day</span>
			<br />
			<NavLink class="go" to={`/request/${lastSegment(window.location.href)}`}>Rent now</NavLink>

			{localStorage.getItem('isAdmin') === 'true' && <h3>Payments:</h3>}
			{localStorage.getItem('isAdmin') === 'true' && paymentsToTotal(this.state.payments)}
			<br />
			{localStorage.getItem('isAdmin') === 'true' && <table class="payment-stats">
				{this.state.payments.map((payment) => {
					return paymentToComponent(payment);
				})}
			</table>}

		</div>
	}
}

export default CarInfo;
