import React, { Component } from "react";
import { fetchPostJson } from "../utils.js";
import API_SERVER from "../Constants.js";

class Register extends Component {
	constructor(props) {
		super(props);
		this.state = {
			username: "",
			password: "",
			passportId: ""
		};

		this.handleChange = this.handleChange.bind(this);
		this.handleSubmit = this.handleSubmit.bind(this);
	}
	handleChange(event) {
		this.setState({
			[event.target.name]: event.target.value
		});
	}
	handleSubmit(event) {
		fetchPostJson(API_SERVER + "/register", this.state)
		.then((result) => {
			if (result['token'].length > 0) {
				localStorage.setItem("token", result['token']);
				localStorage.setItem("isAdmin", result['isAdmin']);
				console.log(`Got token ${result['token']}`);
				window.location.href = "..";
			} else {
				alert("This passport ID or username is already taken");
			}
		});
		event.preventDefault();
	}
	render() {
		return <div class="account-fill-form">
			<form onSubmit={this.handleSubmit}>
				<label> Username:</label>
				<input type="text" name="username" onChange={this.handleChange} />
				<br/>
				<label> Password:</label>
				<input type="password" name="password" onChange={this.handleChange} />
				<br/>
				<label> Passport ID:</label>
				<input type="text" name="passportId" onChange={this.handleChange} />
				<br/>
				<input type="submit" value="Register" />
			</form>
		</div>
	}
}

export default Register;
