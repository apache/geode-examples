using System;
using Apache.Geode.Client;

namespace Apache.Geode.Examples.AuthInitialize
{
  class ExampleAuthInitialize : IAuthInitialize
  {
    public ExampleAuthInitialize()
    {
      // TODO initialize your resources here
      Console.Out.WriteLine("ExampleAuthInitialize::ExampleAuthInitialize called");
    }

    public void Close()
    {
      // TODO close your resources here
      Console.Out.WriteLine("ExampleAuthInitialize::Close called");
    }

    public Properties<string, object> GetCredentials(Properties<string, string> props, string server)
    {
      // TODO get your username and password
      Console.Out.WriteLine("ExampleAuthInitialize::GetCredentials called");

      var credentials = new Properties<string, object>();
      credentials.Insert("username", "john");
      credentials.Insert("password", "secret");
      return credentials;
    }
  }
}
