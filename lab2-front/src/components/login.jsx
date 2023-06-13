import React, { Component } from "react";
import { NavLink } from "react-router-dom";
import { fetchPostJson } from "../utils.js";
import API_SERVER from "../Constants.js";

class Login extends Component {
	constructor(props) {
		super(props);
		this.state = {
			username: "",
			password: ""
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
		console.log("Logging in");
		localStorage.removeItem('token');
		fetchPostJson(API_SERVER + "/login", this.state)
		.then((result) => {
			console.error(result);
			if (result['token'].length > 0) {
				localStorage.setItem("token", result['token']);
				localStorage.setItem("isAdmin", result['shouldUseAdminFrontend']);
				console.log(`Got token ${result['token']}`);
				window.location.href = "..";
			} else {
				alert("Wrong username or password");
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
				<input type="submit" value="Log in" />
			</form>
			<NavLink to="/register">Register</NavLink>
		</div>
	}
}

export default Login;
