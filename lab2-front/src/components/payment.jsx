import { toLocalDateString } from "../utils.js";

function Payment(props) {
	const priceFormat = new Intl.NumberFormat('uk-UA', { style: 'currency', currency: 'UAH' });
	let type = "";
	switch (props.type) {
		case 1: 
			type = "Repairs";
			break;
		case 2:
			type = "Maintenance";
			break;
		case 3:
			type = "Refund after deinal";
			break;
		case 4:
			type = "Repairs paid by customer";
			break;
		case 5:
			type = "Purchased car";
			break;
	}
	return <tr>
		<td> {priceFormat.format(props.uahAmount)} </td>
		<td> {type} </td>
		<td> {props.manufacturer} {props.model} </td>
		<td> {toLocalDateString(props.time)} </td>
	</tr>;
}

function paymentToComponent(payment) {
	return <Payment
		key={payment.id}
		type={payment.type}
		uahAmount={payment.uahAmount}
		time={payment.time}
		model={payment.car.model}
		manufacturer={payment.car.manufacturer}
	/>
}

function paymentsToTotal(payments) {
	const priceFormat = new Intl.NumberFormat('uk-UA', { style: 'currency', currency: 'UAH' });
	return <b>Total profit: {priceFormat.format(payments
		.map((payment) => { return payment.uahAmount })
		.reduce((a, b) => { return a + b }, 0)
	)}</b>
}

export { paymentToComponent, paymentsToTotal };
