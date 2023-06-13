async function fetchPostJson(url, object) {
	const options = {
		method: 'POST',
		body: JSON.stringify(object),
		header: {
			'Content-Type': 'application/json'
		}
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

async function fetchGetJson(url) {
	const response = await fetch(url);
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
	return date.toISOString().split('T')[0];
}

export { fetchPostJson, toLocalDateString, fetchGetJson, lastSegment };
