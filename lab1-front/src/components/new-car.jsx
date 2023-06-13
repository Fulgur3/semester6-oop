import React, { Component } from "react";
import { fetchPostJson } from "../utils.js";
import API_SERVER from "../Constants.js";

class NewCar extends Component {
	constructor(props) {
		super(props);
		this.state = {
			model: "",
			manufacturer: "",
			thumbnailUrl: "",
			description: "",
			hrnPerDay: "",
			hrnPurchase: ""
		};
		this.handleChange = this.handleChange.bind(this);
		this.handleSubmit = this.handleSubmit.bind(this);
	}
	handleChange(event) {
		const target = event.target;
		this.setState({
			[target.name]: target.value
		});
	}
	handleSubmit(event) {
		fetchPostJson(API_SERVER + "/new-car", {
			token: localStorage.getItem('token'),
			model: this.state.model,
			manufacturer: this.state.manufacturer,
			thumbnailUrl: this.state.thumbnailUrl,
			description: this.state.description,
			hrnPerDay: this.state.hrnPerDay,
			hrnPurchase: this.state.hrnPurchase,
		})
		.then((result) => {
			if (result['success']) {
				alert("Added new car.");
				window.location.href = '..';
			} else {
				alert("Something went wrong.");
			}
		});
		event.preventDefault();
	}
	render() {
		return <div>
			<form onSubmit={this.handleSubmit}>
				<label>
					Manufacturer
					<input type="text" name="manufacturer" onChange={this.handleChange} />
				</label>
				<br/>
				<label>
					Model
					<input type="text" name="model" onChange={this.handleChange} />
				</label>
				<br/>
				<label>
					UAH/day
					<input type="text" name="hrnPerDay" onChange={this.handleChange} />
				</label>
				<br/>
				<label>
					Image URL
					<input type="text" name="thumbnailUrl" onChange={this.handleChange} />
				</label>
				<br/>
				<label>
					<textarea rows="5" cols="60" name="description" placeholder="Description" onChange={this.handleChange} />
				</label>
				<br/>
				<label>
					Initial purchase cost (UAH)
					<input type="text" name="hrnPurchase" onChange={this.handleChange} />
				</label>
				<input type="submit" value="Add car" />
			</form>
		</div>
	}
}

export default NewCar;
