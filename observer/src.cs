using System;
using System.Collections.Generic;
using System.Text;

namespace observer
{
    class Observable
    {
        private List<Observer> observers;

        public Observable()
        {
            observers = new List<Observer>();
        }

        public void addObserver(Observer observer)
        {
            observers.Add(observer);
        }

        public void removeObserver(Observer observer)
        {
            observers.Remove(observer);
        }

        public void dataChanged()
        {
            foreach (Observer observer in observers)
            {
                observer.update(this);
            }
        }
    }

    interface Observer
    {
        void update(Observable invoker);
    }

    class DestributedRepository : Observable, Observer
    {
        private String data;

        public DestributedRepository()
        {

        }

        public void setData(String data)
        {
            this.data = data;
            dataChanged();
        }

        public String getData()
        {
            return data;
        }

        public void update(Observable invoker)
        {
            synchronizeWith((DestributedRepository)invoker);
        }

        public void synchronizeWith(DestributedRepository repository)
        {
            this.data = repository.getData();
        }

    }

    class DRFactory
    {
        private List<DestributedRepository> repositories;

        public DRFactory()
        {
            this.repositories = new List<DestributedRepository>();
        }

        public DestributedRepository createRepository()
        {
            DestributedRepository dr = new DestributedRepository();

            if (repositories.Count != 0)
            {
                dr.synchronizeWith(repositories[0]);
            }

            foreach (DestributedRepository repo in repositories) 
            {
                repo.addObserver(dr);
                dr.addObserver(repo);
            }

            repositories.Add(dr);

            return dr;
        }
    }


    class Program
    {
        static void Main(string[] args)
        {
            DRFactory factory = new DRFactory();

            DestributedRepository[] repos = { 
                factory.createRepository(), factory.createRepository(), factory.createRepository(), 
                factory.createRepository(), factory.createRepository(), factory.createRepository()
            };

            repos[0].setData("some data");

            foreach (DestributedRepository repo in repos)
            { 
                System.Console.WriteLine(repo.getData());
            }

        }
    }
}
