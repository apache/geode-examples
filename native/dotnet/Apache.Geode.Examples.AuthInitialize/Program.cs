using System;
using Apache.Geode.Client;

namespace Apache.Geode.Examples.AuthInitialize
{
  class Program
  {
    static void Main(string[] args)
    {
      var cacheFactory = CacheFactory.CreateCacheFactory()
          .Set("log-level", "none")
          .SetAuthInitialize(new ExampleAuthInitialize());

      var cache = cacheFactory.Create();
      var poolFactory = cache.GetPoolFactory()
          .AddLocator("localhost", 10334);
      poolFactory.Create("pool", cache);
      var regionFactory = cache.CreateRegionFactory(RegionShortcut.PROXY)
          .SetPoolName("pool");
      var region = regionFactory.Create<string, string>("region");

      region["a"] = "1";
      region["b"] = "2";

      var a = region["a"];
      var b = region["b"];

      Console.Out.WriteLine("a = " + a);
      Console.Out.WriteLine("b = " + b);

      cache.Close();
    }
  }
}
