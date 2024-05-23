using System.Management.Automation;
using Newtonsoft.Json.Linq;

namespace Cmdlets
{
    internal record Airline
    {
        public string Name { get; set; }
        public string Code { get; set; }

    }

    [Cmdlet(VerbsCommon.Get, "Airlines")]
    public class GetAirlinesCmdlet : Cmdlet
    {
        const string SearchAirlineUrl = "https://www.flightradar24.com/v1/search/web/find?type=operator&query=";

        [Parameter(Mandatory = true, Position = 0)]
        public string Name { get; set; }

        protected override void ProcessRecord()
        {
            var airlines = SearchAirlineByName(Name).GetAwaiter().GetResult();
            WriteObject(airlines);
        }

        private async Task<IList<Airline>> SearchAirlineByName(string name)
        {
            using (HttpClient client = new HttpClient())
            {
                IList<Airline> airlines = new List<Airline>();
                dynamic body = await client.GetStringAsync(SearchAirlineUrl + name.ToUpper());
                dynamic payload = JObject.Parse(body);
                foreach (dynamic result in payload.results)
                {
                    var airline = new Airline { Name = result.name, Code = result.id };
                    airlines.Add(airline);
                }
                return airlines;
            }
        }
    }
}
