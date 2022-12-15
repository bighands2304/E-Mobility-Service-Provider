// SIGNATURES

abstract sig Boolean {}
one sig TRUE extends Boolean {}
one sig FALSE extends Boolean {}

sig Username {}
sig Password {}
sig EmailAddress {}

sig Position {
	latitude: one Int,
	longitude: one Int
} {
	latitude > 0 and longitude > 0
}

sig DateTime {
	timestamp: one Int
} { 
	timestamp > 0 
}

sig User {
	username: one Username,
	password: one Password,
	email: one EmailAddress,
	stationResearches: set StationsResearch,
	reservations: set Reservation,
	vehicles: set Vehicle,
	notifications: set Notification,
	paymentMethods: set PaymentMethod,
	payments: set Payment
}

sig StationsResearch {
	timestamp: one DateTime,
	position: one Position,
	nearbyStations: set ChargingPoint,
	distanceRange: one Int
} {
	distanceRange > 0
}

sig Reservation {
	from: one DateTime,
	to: one DateTime,
	socket: one Socket,
	chargingSession: lone ChargingSession
} {
	from.timestamp < to.timestamp
}

sig ChargingSession {
	isFinished: one Boolean,
	energyConsumed: one Int,
	batteryStatusEstimation: one Int,
} {
	energyConsumed >= 0 and
	batteryStatusEstimation >= 0
}

sig Vehicle {
	vin: one Int,
	model: one CarModel,
	battery: one VehicleBattery
} {
	vin >= 0
}

sig CarModel {}
sig VehicleBattery {
	status: one Int
} {
	status > 0
}

abstract sig Notification {
	timestamp: one DateTime
}

sig ChargingEndedNotification extends Notification {
	chargingSession: one ChargingSession
}

sig ChargingSuggestion extends Notification {
	from: one DateTime,
	to: one DateTime,
	suggestedVehicle: one Vehicle,
	chargingPoint: one ChargingPoint
} {
	from.timestamp < to.timestamp and 
	timestamp.timestamp <= from.timestamp
}

sig PaymentMethod {}
sig Payment {
	amount: one Int,
	paymentMethod: one PaymentMethod,
	CPOInfo: one CPOPaymentReceiver,
	chargingSession: one ChargingSession
} {
	amount > 0
}

sig CPO {
	chargingPoints: set ChargingPoint,
	paymentReceiverInfo: one CPOPaymentReceiver
}

sig CPOPaymentReceiver {}

sig ChargingPoint {
	sockets: set Socket,
	position: one Position,
	availableDSO: set DSOEnergySource,
	batteries: set ChargingStationBattery,
	currentEnergySource: one EnergySource,
	tariffs: set Tariff
} {
	#availableDSO > 0
	#sockets > 0
	#tariffs > 0
}

sig Tariff {
	unitPrice: one Int,
	socketType: one SocketType
} {
	unitPrice > 0
}

sig SpecialOffer extends Tariff {
	validFrom: one DateTime,
	expiration: one DateTime
} {
	validFrom.timestamp < expiration.timestamp
}

sig Socket {
	type: one SocketType,
	chargingProfile: one ChargingProfile, 
	status: one SocketStatus,
	reservations: set Reservation
}

sig ChargingProfile {
	validFrom: one DateTime,
	validTo: one DateTime,
	schedules: set ChargingSchedulePeriod
} {
	validFrom.timestamp < validTo.timestamp
}

sig ChargingSchedulePeriod {
	startTime: one Int, //time local to the charging profile
	powerLimit: one Int
} {
	powerLimit > 0 and startTime > 0
}

abstract sig SocketType {}
one sig SLOW extends SocketType {}
one sig FAST extends SocketType {}
one sig RAPID extends SocketType {} 

abstract sig SocketStatus {}
one sig AVAILABLE extends SocketStatus {}
one sig RESERVED extends SocketStatus {}
one sig NOT_AVAILABLE extends SocketStatus {} 
one sig CHARGING extends SocketStatus {} 

abstract sig EnergySource {
	maxEnergyFlow: one Int,
	actualEnergyFlow: one Int
} {
	maxEnergyFlow > 0 and actualEnergyFlow > 0 and
	actualEnergyFlow <= maxEnergyFlow
}

sig DSOEnergySource extends EnergySource {
	dso: one DSO,
	energyOffer: set DSOEnergyOffer
}

sig ChargingStationBattery extends EnergySource {
	capacity: one Int,
	storageStatus: one Int,
	isInUse: one Boolean,
	isRecharging: one Boolean
} {
	capacity > 0 and storageStatus > 0 and
	(isInUse = TRUE implies isRecharging = FALSE) and
	(isRecharging = TRUE implies isInUse = FALSE)
}

sig DSO {}

sig DSOEnergyOffer {
	price: one Int,
	startTime: one DateTime,
	endTime: one DateTime
} {
	price > 0 and 
	startTime.timestamp < endTime.timestamp
} 

-------------------------------------------------------------------------------
// FUNCTIONS

fun sockets [c: CPO] : set Socket {
	c.chargingPoints.sockets
}

fun payedSessions[u: User]: set ChargingSession {
	u.payments.chargingSession
}

-------------------------------------------------------------------------------
// UTILITY PREDICATES

pred reservationsOverlapping[r1: Reservation, r2: Reservation] {
	r1. from.timestamp >= r2.from.timestamp and r1.from.timestamp < r2.to.timestamp
}

// For simplicity, the range of a research here is a square and not a circle
pred nearRange[sr: StationsResearch, cp: ChargingPoint] {
	((cp.position.latitude >= sr.position.latitude and 
	cp.position.latitude - sr.position.latitude <= sr.distanceRange) or
	(cp.position.latitude < sr.position.latitude and 
	sr.position.latitude - cp.position.latitude <= sr.distanceRange)) and
	((cp.position.longitude >= sr.position.longitude and
	cp.position.longitude - sr.position.longitude <= sr.distanceRange) or
	(cp.position.longitude < sr.position.longitude and
	sr.position.longitude - cp.position.longitude <= sr.distanceRange))
}

--------------------------------------------------------------------------
// FACTS

fact uniqueUsername {
	no disj u1, u2: User | u1.username = u2.username
}

fact uniqueEmail {
	no disj u1, u2: User | u1.email = u2.email
}

fact allPasswordsHaveUser {
	all p: Password | (some u: User | u.password = p)
}

fact allEmailAddressHaveUser {
	all e: EmailAddress | (some u: User | u.email = e)
}

fact allUsernameHaveUser {
	all usn: Username | (some u: User | u.username = usn)
}

fact noOverlappingReservationsOfUser {
	no disj r1, r2: Reservation | reservationsOverlapping[r1, r2] and
			(some u: User | r1 in u.reservations and r2 in u.reservations)
}

fact allReservationsHaveExactlyOneUser {
	all r: Reservation | 
		(no disj u1, u2: User | r in u1.reservations and r in u2.reservations) and (some usr: User | r in usr.reservations)
}

fact reservationSocketConsistency {
	all r: Reservation, s: Socket | r.socket = s implies 
		(r in s.reservations and (no r.chargingSession implies s.status = RESERVED) and 
		(r.chargingSession.isFinished = FALSE implies s.status = CHARGING))
	all s: Socket | 
		(all r: Reservation | not r in s.reservations or r.chargingSession.isFinished = TRUE) 
		iff (s.status = AVAILABLE or s.status = NOT_AVAILABLE)
	no disj r1, r2: Reservation | 
		reservationsOverlapping[r1, r2] and r1.socket = r2.socket
	all s: Socket, r: Reservation | (r.socket = s and no r.chargingSession) implies
		(all r2: Reservation | r2 in s.reservations and (r2 = r or
			(r2.chargingSession.isFinished = TRUE and r2.to.timestamp < r.from.timestamp)))
}

fact allStationsResearchesHaveUser {
	all sr: StationsResearch | (one u: User | sr in u.stationResearches)
}

fact stationsResarchesNearbyStations {
	all sr: StationsResearch, cp: ChargingPoint | 
		nearRange[sr, cp] implies cp in sr.nearbyStations
}

fact oneChargingSessionPerReservation {
	all cs: ChargingSession | one r: Reservation | r.chargingSession = cs
}

fact oneNotificationPerChargingSession {
	no disj n1, n2: ChargingEndedNotification | n1.chargingSession = n2.chargingSession
}

fact allChargingSessionHaveAtMostOnePayment {
	no disj p1, p2: Payment | p1.chargingSession = p2.chargingSession
}

fact paymentChargingSessionFinished {
	all pm: Payment | pm.chargingSession.isFinished = TRUE
}

fact paymentReservationSameUser {
	all u: User, pm: Payment, r: Reservation | 
		(pm in u.payments and pm.chargingSession = r.chargingSession) implies r in u.reservations
}

fact paymentChargingSessionSameCPO {
	all cpo: CPO, pm: Payment | cpo.paymentReceiverInfo = pm.CPOInfo implies 
		(one r: Reservation | r.chargingSession = pm.chargingSession and r.socket in cpo.sockets)
}

fact allPaymentsHaveOneUser {
	all p: Payment | (one u: User | p in u.payments)
}

fact allPaymentMethodHaveUserOrPayment {
	all pm: PaymentMethod |  (some u: User | pm in u.paymentMethods) or 
				(some p: Payment | p.paymentMethod = pm)
}

fact allVehicleHaveUser {
	all v: Vehicle | (some u: User | v in u.vehicles)
}

fact allCarModelHaveVehicle {
	all cm: CarModel | (some v: Vehicle | v.model = cm)
}

fact allVehicleBatteriesHaveOnlyOneVehicle {
	all vb: VehicleBattery | (one v: Vehicle | v.battery = vb)
}

fact uniqueVIN {
	no disj v1, v2: Vehicle | v1.vin = v2.vin
}

fact allNotificationsHaveUser {
	all n: Notification | (one u: User | n in u.notifications)
}

fact suggestionsVehicleOfUser {
	all usr: User, cs: ChargingSuggestion |
		(cs in usr.notifications) implies (cs.suggestedVehicle in usr.vehicles)
}

fact uniquePositionForChargingPoints {
	no disj s1, s2: ChargingPoint | s1.position = s2.position
}

fact oneCPOPerPaymentReceiver {
	all pr: CPOPaymentReceiver | (one cpo: CPO | cpo.paymentReceiverInfo = pr)
}

fact oneCPOPerChargingPoint {
	all s: ChargingPoint | 
		(no disj c1, c2: CPO | s in c1.chargingPoints and s in c2.chargingPoints)
}

fact oneChargingStationPerSocket {
	all sock: Socket | 
		(no disj p1, p2: ChargingPoint | sock in p1.sockets and sock in p2.sockets)
}

fact allSocketsHaveChargingPoint {
	all sock: Socket | (some cp: ChargingPoint | sock in cp.sockets)
}

fact allChargingStationsHaveCPO {
	all cp: ChargingPoint | (one cpo: CPO | cp in cpo.chargingPoints)
}

fact allChargingSchedulePeriodHaveChargingProfile {
	all csp: ChargingSchedulePeriod | 
		(some cp: ChargingProfile | csp in cp.schedules)
}

fact noChargingSchedulePeriodWithSameStartInsideChargingProfile {
	all cp: ChargingProfile | no disj csp1, csp2: ChargingSchedulePeriod |
		csp1 in cp.schedules and csp2 in cp.schedules and csp1.startTime = csp2.startTime
}

fact allChargingProfilesHaveSocket {
	all cp: ChargingProfile | (some sock: Socket | sock.chargingProfile = cp)
}

fact currentEnergySourceIsInBatteriesOrAvailableDSO {
	all cp: ChargingPoint, dso: DSOEnergySource |
		dso = cp.currentEnergySource implies dso in cp.availableDSO
	all cp: ChargingPoint, bat: ChargingStationBattery |
		bat = cp.currentEnergySource implies bat in cp.batteries
}

fact allBatteriesHaveExactlyOneChargingPoint {
	all bat: ChargingStationBattery | (one cp: ChargingPoint | bat in cp.batteries)
}

fact allDSOEnergySourceHaveChargingPoint {
	all des: DSOEnergySource | some cp: ChargingPoint | des in cp.availableDSO
}

fact allDSOEnergyOfferHaveOnlyOneDSOEnergySource {
	all deo: DSOEnergyOffer | (one des: DSOEnergySource | des.energyOffer = deo)
}

fact chargingPointAtMostOneEnergySourceOfDSO {
	all cp: ChargingPoint | 
		(no disj des1, des2: DSOEnergySource | 
			des1.dso = des2.dso and des1 in cp.availableDSO and des2 in cp.availableDSO)
}

fact allTariffHaveOnlyOneChargingPoint {
	all tar: Tariff | (one cp: ChargingPoint | tar in cp.tariffs)
}

//All type of sockets in a charging point must have a tariff
fact socketTariff {
	all sock: Socket, cp: ChargingPoint | 
		sock in cp.sockets implies (some t: Tariff | t in cp.tariffs and t.socketType = sock.type) 
}

//The user must insert at least a vehicle in order to receive suggestions.
fact vehicleForSuggestions {
	all cs: ChargingSuggestion, u: User | 
		cs in u.notifications implies #u.vehicles > 0
}

//The system shall prevent the user to make new reservations if there are unsolved payment. 
fact reservationNoUnsolvedPayment {
	all u: User | 
		(some r: Reservation | no r.chargingSession and r in u.reservations) 
			implies (all cs: ChargingSession | cs in u.reservations.chargingSession 
				implies (cs in u.payedSessions or not cs.isFinished = TRUE))
}

//The system shall allow the user to have only one reservation active at the same time.
fact onlyOneActiveReservation {
	all u: User | (no disj r1, r2: Reservation | r1 in u.reservations and r2 in u.reservations and 
		(no r1.chargingSession or r1.chargingSession.isFinished = FALSE) and 
		(no r2.chargingSession or r1.chargingSession.isFinished = FALSE))
}

------------------------------------------------------------------------------------
//PREDICATES FOR DYNAMIC MODELLING

pred makeReservation[u, u': User, r: Reservation] {
	//precondition
	no r.chargingSession
		
	//postconditions
	u'.reservations = u.reservations + r
	u'.username = u.username
	u'.password = u.password
	u'.email = u.email
	u'.stationResearches = u.stationResearches
	u'.vehicles = u.vehicles
	u'.notifications = u.notifications
	u'.paymentMethods = u.paymentMethods
	u'.payments = u.payments
}
run makeReservation for 4 but exactly 2 Reservation, 1 User

pred addStationsResearch[u, u': User, sr: StationsResearch] {
	u'.reservations = u.reservations
	u'.username = u.username
	u'.password = u.password
	u'.email = u.email
	u'.stationResearches = u.stationResearches + sr
	u'.vehicles = u.vehicles
	u'.notifications = u.notifications
	u'.paymentMethods = u.paymentMethods
	u'.payments = u.payments
}
run addStationsResearch for 4 but exactly 2 ChargingPoint

pred payChargingSession[u, u': User, cs: ChargingSession, p: Payment] {
	//preconditions
	p.chargingSession = cs
	one r: Reservation | r in u.reservations and r.chargingSession = cs
	no p1: Payment | p1 in u.payments and p1.chargingSession = cs and p1 != p

	//postconditions
	u'.reservations = u.reservations
	u'.username = u.username
	u'.password = u.password
	u'.email = u.email
	u'.stationResearches = u.stationResearches
	u'.vehicles = u.vehicles
	u'.notifications = u.notifications
	u'.paymentMethods = u.paymentMethods
	u'.payments = u.payments + p
}
run payChargingSession

pred setChargingPointDSO [cp, cp': ChargingPoint, dso: DSOEnergySource] {
	//precondition
	dso in cp.availableDSO

	//postconditions
	cp'.sockets = cp.sockets
	cp'.position = cp.position
	cp'.availableDSO = cp.availableDSO
	cp'.batteries = cp.batteries
	cp'.tariffs = cp.tariffs
	cp'.currentEnergySource = dso
}
run setChargingPointDSO for 4 but exactly 3 DSOEnergySource, 1 ChargingPoint

pred addTariffToChargingPoint [cp, cp': ChargingPoint, t: Tariff] {
	//preconditions
	some sock: Socket | sock in cp.sockets and sock.type = t.socketType

	//postconditions
	cp'.sockets = cp.sockets
	cp'.position = cp.position
	cp'.availableDSO = cp.availableDSO
	cp'.batteries = cp.batteries
	cp'.tariffs = cp.tariffs + t
	cp'.currentEnergySource = cp.currentEnergySource
}
run addTariffToChargingPoint

-------------------------------------------------------------------------------
// ASSERTIONS

//Goal: allow users to visualize nearby charging stations
assert allowStationsResearches {
	all sr: StationsResearch | (some u: User | sr in u.stationResearches) and 
		((some cp: ChargingPoint | nearRange[sr, cp]) implies #sr.nearbyStations > 0)
}
check allowStationsResearches for 4

// Goal: allow users to book a charge
// There might be some reservations that do not have a charging session
assert bookCharge {
	all r: Reservation | 
		not (some cs: ChargingSession | cs = r.chargingSession) implies (one u: User | r in u.reservations)
}
check bookCharge for 4

// Goal: allow a user to start a charging session
assert startChargingSession {
	all cs: ChargingSession | 
		(some u: User, r: Reservation | r in u.reservations and r.chargingSession = cs)
}
check startChargingSession for 4

// Goal: allow users to pay for the service
assert payForService {
	all p: Payment | p.chargingSession.isFinished = TRUE and 
					(one u: User | p in u.payments and p.chargingSession in u.reservations.chargingSession)
}
check payForService for 4

//Goal: allow users to receive suggestions
assert userSuggestions {
	all cs: ChargingSuggestion | (some u: User | cs in u.notifications)
}
check userSuggestions for 4

//This assertion checks if the model for the reservations and payment is correct
assert testConsistency {
	not (#User = 1 and #ChargingSession = 2 and #Reservation = 4 and #Payment = 2)
}
check testConsistency for 6

-------------------------------------------------------------------------------
// WORLDS

//General world with multiple Users and CPOs
pred world1 {
	#User = 2
	#CPO = 2
	#ChargingPoint >= 3
	#Reservation >= 4
	#Socket >= 4
	some s: Socket | s.type = SLOW
	some s: Socket | s.type = FAST
	some s: Socket | s.type = RAPID
	#ChargingSession > 2
	some cs: ChargingSession | cs.isFinished = TRUE
	#Payment > 1
}
run world1 for 4

//This world is focused on charging suggestions
pred world2 {
	#Vehicle > 2
	#ChargingSuggestion > 3
	#User > 2
	#CPO = 1
	#Reservation = 0
}
run world2 for 6

//This world is focused on charging points
pred world3 {
	#User = 0
	#CPO >= 2
	#ChargingPoint > 2
	#ChargingStationBattery > 0
	#DSOEnergySource > 3
	#Tariff > 4
	#SpecialOffer > 0
}
run world3 for 8

//This world is focused on the user registration
pred world4 {
	#Vehicle > 3
	#User > 2
	#CPO = 0
	some u: User | #u.vehicles > 1
	some u: User | #u.paymentMethods > 1
	#DateTime = 0
}
run world4 for 4

//This world is focused on reservations on the same socket
pred world5 {
	#Socket = 1
	#User > 2
	#Reservation >= 3
	some r: Reservation | no r.chargingSession
	#Payment >= 2
}
run world5 for 6

//This world is focused on the stations researches
pred world6 {
	#User > 0
	#StationsResearch > 2
	#CPO = 1
	#ChargingPoint >= 5
	all cp: ChargingPoint | #cp.sockets = 1
	#Reservation = 0
}
run world6 for 6
