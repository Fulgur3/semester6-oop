import React, { Component } from "react";
import { toLocalDateString, fetchGetJson, fetchPostJson, lastSegment } from "../utils.js";
import API_SERVER from "../Constants.js";

class Request extends Component {
	constructor(props) {
		super(props);
		this.state = {
			days: 0,
			startDate: "",
			uahPerDay: 0,
			cost: 0
		};

		this.carId = lastSegment(window.location.href);

		fetchGetJson(API_SERVER + `/car/${this.carId}`)
		.then((result) => {
			this.setState({
				uahPerDay: result['uahPerDay']
			});
		});

		this.handleChange = this.handleChange.bind(this);
		this.handleSubmit = this.handleSubmit.bind(this);
	}
	handleChange(event) {
		const target = event.target;
		this.setState({
			[target.name]: target.value
		});
		if (target.name === 'days') {
			this.setState({
				cost: this.state.uahPerDay * target.value
			});
		}
	}
	handleSubmit(event) {
		fetchPostJson(API_SERVER + "/requests/new", {
			token: localStorage.getItem('token'),
			carId: this.carId,
			days: this.state.days,
			startDate: toLocalDateString(new Date()),
			paymentUah: String(this.state.cost)
		})
		.then((_result) => {
			alert("Thanks! Please wait until your request gets approved.");
			window.location.href = '..';
		});
		event.preventDefault();
	}
	render() {
		const priceFormat = new Intl.NumberFormat('uk-UA', { style: 'currency', currency: 'UAH' });

		return <div>
			<form onSubmit={this.handleSubmit}>
				<label>
					How many days?
					<input type="number" min="1" max="31" name="days" onChange={this.handleChange} />
				</label>
				<br/>
				<div class="price">Total cost: <b>{priceFormat.format(this.state.cost)}</b></div>
				<input type="submit" value="Proceed with payment" />
			</form>
		</div>
	}
}

export default Request;
