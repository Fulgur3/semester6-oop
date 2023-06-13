import React, { Component } from "react";
import { fetchGetJson } from "../utils.js";
import API_SERVER, { STATUS_REPAIR_NEEDED, STATUS_DENIED, STATUS_PENDING, STATUS_ACTIVE } from "../Constants.js";
import { requestInfoToComponent } from "./request.jsx";

class MyStatus extends Component {
	constructor(props) {
		super(props);
		this.state = {
			pendingRequests: [],
			activeRequests: [],
			deniedRequests: [],
			repairNeededRequests: [],
			outdatedRequests: []
		}
		this.refreshData = this.refreshData.bind(this);
		this.refreshData();
	}

	refreshData() {
		fetchGetJson(API_SERVER + `/requests/status/my/${STATUS_DENIED}`)
		.then((result) => {
			console.log(result);
			this.setState({
				deniedRequests: result
			});
		});

		fetchGetJson(API_SERVER + `/requests/status/my/${STATUS_REPAIR_NEEDED}`)
		.then((result) => {
			console.log(result);
			this.setState({
				repairNeededRequests: result
			});
		});

		fetchGetJson(API_SERVER + `/requests/status/my/${STATUS_PENDING}`)
		.then((result) => {
			console.log(result);
			this.setState({
				pendingRequests: result
			});
		});

		fetchGetJson(API_SERVER + `/requests/status/my/${STATUS_ACTIVE}`)
		.then((result) => {
			console.log(result);
			this.setState({
				activeRequests: result
			});
		});

		fetchGetJson(API_SERVER + `/requests/my-outdated/`)
		.then((result) => {
			console.log(result);
			this.setState({
				outdatedRequests: result
			});
		});
	}
	render() {
		return <div>
			<h2>Repair needed:</h2>
			<div class="requests">
				{this.state.repairNeededRequests.map((requestInfo) => {
					return requestInfoToComponent(requestInfo, this.refreshData);
				})}
			</div>
			<h2>Pending requests:</h2>
			<div class="requests">
				{this.state.pendingRequests.map((requestInfo) => {
					return requestInfoToComponent(requestInfo, this.refreshData);
				})}
			</div>
			<h2>Active requests:</h2>
			<div class="requests">
				{this.state.activeRequests.map((requestInfo) => {
					return requestInfoToComponent(requestInfo, this.refreshData);
				})}
			</div>
			<h2>Denied requests:</h2>
			<div class="requests">
				{this.state.deniedRequests.map((requestInfo) => {
					return requestInfoToComponent(requestInfo, this.refreshData);
				})}
			</div>
			<h2>Outdated requests:</h2>
			<div class="requests">
				{this.state.outdatedRequests.map((requestInfo) => {
					return requestInfoToComponent(requestInfo, this.refreshData);
				})}
			</div>
		</div>
	}
}

export default MyStatus;
