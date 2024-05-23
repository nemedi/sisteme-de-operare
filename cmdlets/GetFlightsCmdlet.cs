using System.Management.Automation;
using Newtonsoft.Json.Linq;

namespace Cmdlets
{
    internal record Flight
    {
        public string Number { get; set; }
        public string Origin { get; set; }
        public string Destination { get; set; }
        public string Aircraft { get; set; }
        public string Airline { get; set; }
        public string Status { get; set; }
    }

    [Cmdlet(VerbsCommon.Get, "Flights")]
    public class GetFlightsCmdlet : Cmdlet
    {
        const string SearchFlightsUrl = "https://data-cloud.flightradar24.com/zones/fcgi/feed.js?airline=";
        const string SearchFlightUrl = "https://data-live.flightradar24.com/clickhandler/?flight=";

        [Parameter(Mandatory = true, Position = 0)]
        public string Airline { get; set; }

        protected override void ProcessRecord()
        {
            var flights = SearchFlightsByAirlineCode(Airline).GetAwaiter().GetResult();
            WriteObject(flights);
        }


        private async Task<IList<Flight>> SearchFlightsByAirlineCode(string airlineCode)
        {
            using (HttpClient client = new HttpClient())
            {
                IList<Flight> flights = new List<Flight>();
                dynamic body = await client.GetStringAsync(SearchFlightsUrl + airlineCode.ToUpper());
                JObject payload = JObject.Parse(body);
                foreach (var entry in payload)
                {
                    if (entry.Value is JArray)
                    {
                        var flight = await SearchFlightByCode(entry.Key);
                        flights.Add(flight);
                    }
                }
                return flights;
            }
        }

        private async Task<Flight> SearchFlightByCode(string flightCode)
        {
            using (HttpClient client = new HttpClient())
            {
                dynamic body = await client.GetStringAsync(SearchFlightUrl + flightCode);
                dynamic payload = JObject.Parse(body);
                var flight = new Flight
                {
                    Number = payload.identification?.number["default"],
                    Airline = payload.airline?.name,
                    Aircraft = payload.aircraft?.model?.text,
                    Origin = payload.airport?.origin?.name,
                    Destination = payload.airport?.destination?.name,
                    Status = payload.status?.text,
                };
                return flight;
            }
        }
    }
}
