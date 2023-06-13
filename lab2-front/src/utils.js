async function fetchPostJson(url, object) {
	const options = {
		method: 'POST',
		body: JSON.stringify(object),
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		}
	}
	if (localStorage.getItem('token')) {
		options.headers['Authorization'] = 'Bearer ' + localStorage.getItem("token")
	}
	const response = await fetch(url, options);
	const text = await response.text();
	console.log(`Response: ${text}`);
	try {
		if (text)
			return JSON.parse(text);
		else
			return {};
	} catch (e) {
		console.error("Could not parse response");
		throw e;
	}
}

async function fetchGetJson(url) {
	let options = {
		headers: {}
	}
	if (localStorage.getItem('token')) {
		options.headers['Authorization'] = 'Bearer ' + localStorage.getItem("token")
	}
	const response = await fetch(url, options);
	const text = await response.text();
	console.log(`Response: ${text}`);
	try {
		return JSON.parse(text);
	} catch (e) {
		console.error("Could not parse response");
		throw e;
	}
}

function lastSegment(url) {
	const parts = url.split('/');
	return parts.pop() || parts.pop();  // handle potential trailing slash
}

function toLocalDateString(date) {
	let dateString = date;
	if (typeof date !== 'string')
		dateString = date.toISOString();
	return dateString.split('T')[0];
}

export { fetchPostJson, toLocalDateString, fetchGetJson, lastSegment };
