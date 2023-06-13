import React, { Component } from "react";
import { fetchPostJson } from "../utils.js";
import API_SERVER, { STATUS_REPAIR_NEEDED, STATUS_PENDING, STATUS_ACTIVE } from "../Constants.js";
import { requestInfoToComponent } from "./request.jsx";

class RequestsAdmin extends Component {
	constructor(props) {
		super(props);
		this.state = {
			pendingRequests: [],
			activeRequests: [],
			outdatedRequests: [],
			repairNeededRequests: []
		}
		this.getRequests = this.getRequests.bind(this);
		this.getRequests();
	}
	getRequests() {
		fetchPostJson(API_SERVER + "/list-requests", {
			token: localStorage.getItem('token'),
			status: STATUS_PENDING
		})
		.then((result) => {
			console.log(result);
			this.setState({
				pendingRequests: result['requests']
			});
		});

		fetchPostJson(API_SERVER + "/list-requests", {
			token: localStorage.getItem('token'),
			status: STATUS_ACTIVE
		})
		.then((result) => {
			console.log(result);
			this.setState({
				activeRequests: result['requests']
			});
		});

		fetchPostJson(API_SERVER + "/list-requests", {
			token: localStorage.getItem('token'),
			status: STATUS_REPAIR_NEEDED
		})
		.then((result) => {
			console.log(result);
			this.setState({
				repairNeededRequests: result['requests']
			});
		});

		fetchPostJson(API_SERVER + "/list-requests", {
			token: localStorage.getItem('token'),
			getOutdatedActive: true
		})
		.then((result) => {
			console.log(result);
			this.setState({
				outdatedRequests: result['requests']
			});
		});
	}
	render() {
		return <div>
			<h2>Pending requests:</h2>
			<div class="requests">
				{this.state.pendingRequests.map((requestInfo) => {
					return requestInfoToComponent(requestInfo, this.getRequests);
				})}
			</div>
			<h2>Active requests:</h2>
			<div class="requests">
				{this.state.activeRequests.map((requestInfo) => {
					return requestInfoToComponent(requestInfo, this.getRequests);
				})}
			</div>
			<h2>Repair needed:</h2>
			<div class="requests">
				{this.state.repairNeededRequests.map((requestInfo) => {
					return requestInfoToComponent(requestInfo, this.getRequests);
				})}
			</div>
			<h2>Outdated requests:</h2>
			<div class="requests">
				{this.state.outdatedRequests.map((requestInfo) => {
					return requestInfoToComponent(requestInfo, this.getRequests);
				})}
			</div>
		</div>
	}
}

export default RequestsAdmin;
