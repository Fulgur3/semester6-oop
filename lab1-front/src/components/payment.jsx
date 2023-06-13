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
		<td> {priceFormat.format(props.hrnAmount)} </td>
		<td> {type} </td>
		<td> {props.manufacturer} {props.model} </td>
		<td> {props.date} </td>
	</tr>;
}

function paymentToComponent(payment) {
	return <Payment
		key={payment.id}
		type={payment.type}
		hrnAmount={payment.hrnAmount}
		date={payment.date}
		model={payment.model}
		manufacturer={payment.manufacturer}
	/>
}

function paymentsToTotal(payments) {
	const priceFormat = new Intl.NumberFormat('uk-UA', { style: 'currency', currency: 'UAH' });
	return <b>Total profit: {priceFormat.format(payments
		.map((payment) => { return payment.hrnAmount })
		.reduce((a, b) => { return a + b }, 0)
	)}</b>
}

export { paymentToComponent, paymentsToTotal };
