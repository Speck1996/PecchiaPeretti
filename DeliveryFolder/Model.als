-- Trackme Pecchia Peretti

open util/integer
open util/boolean

sig Location {
	coordX: one Int,
	coordY: one Int
}
{coordX >= -3 and coordX <= 3 and coordY >= -6 and coordY <= 6}

sig Time {
	time: one Int
}
{time >= 0 and time <= 6}

sig Individual {
	hasSOS: one Bool,
	data: lone EHealthData,
	location: lone Location,
	illness: lone Illness
}

sig EHealthData {
	heartRate: lone Int,
	bloodPressure: lone Int,
	steps: lone Int,
	sleepTime: lone Int	
}
{heartRate >= 0 and heartRate <= 6 and bloodPressure >= 0 and bloodPressure <= 6 and
steps >= 0 and steps <= 6 and sleepTime >= 0 and sleepTime <= 6}

sig Illness {
	startTime: one Time
}

sig Ambulance {
	startTime: one Time,
	rescuee: one Individual,
	location: one Location
}

sig EHealthDataGroup {
	data: some EHealthData 
}

sig ThirdParty {
	request: set Request,
	monitor: set Individual,
	groupData: set EHealthDataGroup
}

abstract sig Request {
	approved: one Bool
}

sig GroupRequest extends Request {
	groupData: one EHealthDataGroup
}

sig SpecificRequest extends Request {
	individual: one Individual
}



/*
	Ambulance sending
*/

-- Every EHealthData belongs to a single Individual
fact DataBelongsToSingleIndividual {
	(all d: EHealthData | one i: Individual | i.data = d) 
}

-- There is a Ilness iff threshold have been crossed
fact IllnessIndividual{
	(all i: Individual | i.hasSOS = True and (i.data.heartRate >= 5 or i.data.bloodPressure >= 5) implies one il: Illness | il = i.illness)
	and
	(all il: Illness | one i: Individual | il = i.illness and i.hasSOS = True and (i.data.heartRate >= 5 or i.data.bloodPressure >= 5))
}

-- Prevent two individual with same Illness
fact IllnessSigleIndividual {
	all il: Illness | no disj i1, i2: Individual | il = i1.illness and il = i2.illness
}

-- An ambulance is sent for a good reason with a reaction time of less than 2
fact AmbulanceIndividual {
	all a: Ambulance | a.rescuee.location = a.location and #a.rescuee.illness = 1 and
	a.startTime.time >= a.rescuee.illness.startTime.time and minus[a.startTime.time, a.rescuee.illness.startTime.time] <= 2
}

-- If an Individual is having an illness and he hade activate AutomatedSOS then an Ambulance is directed to his location.
-- Constraint also that only one ambulance has been sent
fact IndividualAmbulance {
	all i: Individual | #i.illness = 1 implies one a: Ambulance | a.rescuee = i
}



/*
	Requests
*/

-- Every request has been sent by a single ThirdParty
fact RequestThirdParty {
	(all r: Request | one t: ThirdParty | r in t.request ) 
}


/* Specific Requests */

-- A third party monitor only individuals who has accepted to be monitored
fact IndividualMonitored {
	all t: ThirdParty | all i: Individual | i in t.monitor implies 
		one r: SpecificRequest | i = r.individual and r.approved = True and r in t.request
}

-- If a SpecificRequest is approved, a ThirdParty is monitoring the specific Individual
fact SpecificRequestApproved {
	all t: ThirdParty | all r: SpecificRequest | r in t.request and r.approved = True implies r.individual in t.monitor
}

-- No multiple SpecificRequest for the same Individual from the same ThirdParty
fact NoMultipleSpecificRequest {
	all t: ThirdParty | no disj r1, r2: SpecificRequest | r1 in t.request and r2 in t.request and r1.individual = r2.individual
}


/* Group Requests */

-- If TrackMe can properly anonymized those a EHealthDataGroup, a GroupRequest to this group is automatically accepted
fact GroupDataPrivacy {
	all r: GroupRequest | #r.groupData.data > 3 implies r.approved = True else r.approved = False
}

-- If a ThirdParty have access to a group of data then there is an approved request for them
fact GroupDataRequest {
	all t: ThirdParty | all g: EHealthDataGroup | g in t.groupData implies (
		one r: GroupRequest | r in t.request and r.groupData = g and r.approved = True)
}

-- If a GroupRequest is accepted, then the ThirdParty have access to EHealthDataGroup
fact {
	all t: ThirdParty | all r: GroupRequest | r in t.request and r.approved = True implies r.groupData in t.groupData
}

-- No multiple GroupRequest for the same group from the same ThirdParty
fact {
	all t: ThirdParty | no disj r1, r2: GroupRequest | r1 in t.request and r2 in t.request and r1.groupData = r2.groupData
}




pred ambulanceWorld {
	some i: Individual | #i.illness > 0 and some a: Ambulance | a.rescuee = i and a.startTime.time > i.illness.startTime.time
}

run ambulanceWorld for 4 but 1 Ambulance, 1 Individual, 1 Illness, 5 Int


pred groupRequestWorld {
	some r1, r2: GroupRequest | r1.approved = True and r2.approved = False
}

run groupRequestWorld for 6 but 2 Request, 1 ThirdParty, 5 Int


pred specificRequestWorld {
	some r1, r2: SpecificRequest | r1.approved = True and r2.approved = False
}

run specificRequestWorld for 5 but 2 Request, 2 Individual, 1 ThirdParty, 2 EHealthData, 0 EHealthDataGroup, 5 Int

assert privacyMaintained {
	no d: EHealthDataGroup | some t: ThirdParty | d in t.groupData and #d.data <= 3
}

check privacyMaintained for 15

assert monitorAccepted {
	no i: Individual | some t: ThirdParty | i in t.monitor and no r: SpecificRequest | r.approved = True and r in t.request
}

check monitorAccepted for 15

assert ambulanceCalling {
	no i: Individual | #i.illness > 0 and no a: Ambulance | a.rescuee = i and a.location = i.location
}

check ambulanceCalling for 15

assert ambulanceReactionTime {
	no i: Individual | #i.illness > 0 and some a: Ambulance | a.rescuee = i and a.location = i.location 
		and minus[a.startTime.time, a.rescuee.illness.startTime.time] > 2
}

check ambulanceReactionTime for 15










