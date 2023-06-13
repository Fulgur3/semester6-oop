import React, { Component } from "react";
import { toLocalDateString, fetchPostJson } from "../utils.js";
import { NavLink } from "react-router-dom";
import API_SERVER  from "../Constants.js";
import { paymentsToTotal, paymentToComponent } from "./payment.jsx";

function PopularityStatistic(props) {
	return <tr>
		<td>
			<NavLink to={`car/${props.carId}`}>
				{props.manufacturer} {props.model}
			</NavLink>
		</td>
		<td>
			{props.requestCount} requests
		</td>
	</tr>;
}

function popularityStatisticToComponent(carStatistic) {
	return <PopularityStatistic
		key={carStatistic.id}
		carId={carStatistic.id}
		manufacturer={carStatistic.manufacturer} 
		model={carStatistic.model}
		requestCount={carStatistic.requestCount}
	/>
}

function ProfitStatistic(props) {
	const priceFormat = new Intl.NumberFormat('uk-UA', { style: 'currency', currency: 'UAH' });
	return <tr>
		<td>
			<NavLink to={`car/${props.carId}`}>
				{props.manufacturer} {props.model}
			</NavLink>
		</td>
		<td>
			{priceFormat.format(props.hrnProfit)}
		</td>
	</tr>;
}

function profitStatisticToComponent(carStatistic) {
	return <ProfitStatistic
		key={carStatistic.id}
		carId={carStatistic.id}
		manufacturer={carStatistic.manufacturer} 
		model={carStatistic.model}
		hrnProfit={carStatistic.hrnProfit}
	/>
}

class AdminStats extends Component {
	constructor(props) {
		super(props);

		const since = new Date();
		since.setUTCDate(since.getUTCDate() - 30);

		this.state = {
			since: toLocalDateString(since),
			recentProfitableCars: [],
			recentPopularCars: [],
			topProfitableCars: [],
			topPopularCars: [],
			allPayments: [],
			recentPayments: []
		}
		this.getStats = this.getStats.bind(this);
		this.handleChange = this.handleChange.bind(this);

		this.getStats();
	}

	getStats() {
		fetchPostJson(API_SERVER + "/top-cars", {
			token: localStorage.getItem('token')
		})
		.then((result) => {
			console.log(result);
			this.setState({
				topProfitableCars: result['carStatistics']
			});
		});
		fetchPostJson(API_SERVER + "/popular-cars", {
			token: localStorage.getItem('token')
		})
		.then((result) => {
			console.log(result);
			this.setState({
				topPopularCars: result['carStatistics']
			});
		});

		fetchPostJson(API_SERVER + "/top-cars", {
			token: localStorage.getItem('token'),
			since: this.state.since
		})
		.then((result) => {
			console.log(result);
			this.setState({
				recentProfitableCars: result['carStatistics']
			});
		});
		fetchPostJson(API_SERVER + "/popular-cars", {
			token: localStorage.getItem('token'),
			since: this.state.since
		})
		.then((result) => {
			console.log(result);
			this.setState({
				recentPopularCars: result['carStatistics']
			});
		});

		fetchPostJson(API_SERVER + "/all-payments", {
			token: localStorage.getItem('token'),
			since: this.state.since
		})
		.then((result) => {
			console.log(result);
			this.setState({
				recentPayments: result['payments']
			});
		});
		fetchPostJson(API_SERVER + "/all-payments", {
			token: localStorage.getItem('token')
		})
		.then((result) => {
			console.log(result);
			this.setState({
				allPayments: result['payments']
			});
		});
	}

	handleChange(event) {
		console.log(event);
		if (event.target.name === 'since') {
			try {
				this.setState({
					since: toLocalDateString(new Date(event.target.value))
				});
				this.getStats();
			} catch (e) {
				if (!(e instanceof RangeError))
					throw e;
			}
		}
	}

	render() {
		return <div>
			<h2>Statistics since <input 
				type="date"
				name="since"
				max={toLocalDateString(new Date())}
				defaultValue={this.state.since}
				onChange={this.handleChange} /> </h2>

			<h3>Most profitable cars:</h3>
			<table class="car-stats">
				{this.state.recentProfitableCars.map((carStatistic) => {
					return profitStatisticToComponent(carStatistic);
				})}
			</table>

			<h3>Most popular cars:</h3>
			<table class="car-stats">
				{this.state.recentPopularCars.map((carStatistic) => {
					return popularityStatisticToComponent(carStatistic);
				})}
			</table>

			<h3>Payments:</h3>
			{paymentsToTotal(this.state.recentPayments)}
			<br />
			<table class="payment-stats">
				{this.state.recentPayments.map((payment) => {
					return paymentToComponent(payment);
				})}
			</table>

			<h2>All-time statistics</h2>

			<h3>Most profitable cars:</h3>
			<table class="car-stats">
				{this.state.topProfitableCars.map((carStatistic) => {
					return profitStatisticToComponent(carStatistic);
				})}
			</table>

			<h3>Most popular cars:</h3>
			<table class="car-stats">
				{this.state.topPopularCars.map((carStatistic) => {
					return popularityStatisticToComponent(carStatistic);
				})}
			</table>

			<h3>Payments:</h3>
			{paymentsToTotal(this.state.allPayments)}
			<br />
			<table class="payment-stats">
				{this.state.allPayments.map((payment) => {
					return paymentToComponent(payment);
				})}
			</table>
		</div>
	}
}

export default AdminStats;
