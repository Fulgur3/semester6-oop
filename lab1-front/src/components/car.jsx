import { NavLink } from "react-router-dom";

function Car(props) {
	const priceFormat = new Intl.NumberFormat('uk-UA', { style: 'currency', currency: 'UAH' });
	return <NavLink to={`car/${props.carId}`}>
		<div class="car">
			<img width="200dp" height="100%" src={props.thumbnailUrl}/>
			<div class="car-text">
				<span class="car-name">{props.manufacturer} {props.model}</span>
				<br />
				<span class="car-cost"><b>{priceFormat.format(props.hrnPerDay)}</b> per day</span>
			</div>
		</div>
	</NavLink>
}

function carToComponent(car) {
	return <Car
		key={car.id}
		carId={car.id}
		manufacturer={car.manufacturer} 
		model={car.model}
		hrnPerDay={car.hrnPerDay}
		thumbnailUrl={car.thumbnailUrl}
	/>
}

export { Car, carToComponent };
