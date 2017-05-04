
/**
 * Write a description of MiniProject here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.io.*;
import edu.duke.*;
import org.apache.commons.csv.*;

public class MiniProject {
    public void printNames() {
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            if (numBorn <= 100) {
                System.out.println("Name " + rec.get(0) + " Gender " + rec.get(1) + " Num Born " + rec.get(2));
            }
        }
    }
    
    public void totalBirths (FileResource fr) {
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            if (rec.get(1).equals("M")) {
                totalBoys += 1;
            } else {
                totalGirls += 1;
            }
        }
        System.out.println("Total Births; " + totalBirths);
        System.out.println("Total Girls; " + totalGirls);
        System.out.println("Total Boys; " + totalBoys);
    }
    
    public int getRank( int year, String name, String gender) {
        // Return the rank of the name in the file for the given gender
        // Rank 1 is the name with the most births.  If the name is not in the list
        // then -1 is returned
        int nameRank = 0;
        Boolean nameFound = false;
        FileResource fr = new FileResource("us_babynames_by_year/yob" + year + ".csv");
        
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (nameFound) {
                // We found our match, break out of the loop
                break;
            }
            
            if (rec.get(1).equals(gender)) {
                // We found the right gender, so increase the gender name count and check the name
                nameRank++;
            
                if (rec.get(0).equals(name)) {
                    // We have matched both gender and name, print out a message and break from the loop
                    System.out.println( "In " + year + ", " + rec.get(0) + " (" + rec.get(1) + ") was ranked number " + nameRank + ".");
                    nameFound = true;
                }
            }
        }
        
        // "Return the value of 'nameRank' if 'nameFound' is true, otherwise return '-1'
        return nameFound ? nameRank : -1;
    }
    
    public int getRank( String name, String gender, FileResource fr) {
        // Return the rank of the name in the file for the given gender
        // Rank 1 is the name with the most births.  If the name is not in the list
        // then -1 is returned
        int nameRank = 0;
        Boolean nameFound = false;
        
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (nameFound) {
                // We found our match, break out of the loop
                break;
            }
            
            if (rec.get(1).equals(gender)) {
                // We found the right gender, so increase the gender name count and check the name
                nameRank++;
            
                if (rec.get(0).equals(name)) {
                    // We have matched both gender and name, print out a message and break from the loop
                    nameFound = true;
                }
            }
        }
        
        // "Return the value of 'nameRank' if 'nameFound' is true, otherwise return '-1'
        return nameFound ? nameRank : -1;
    }
    
    public String getName( int year, int rank, String gender ) {
        FileResource fr = new FileResource("us_babynames_by_year/yob" + year + ".csv");
        String nameAtRank = "NO NAME";
        int curRank = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (rec.get(1).equals(gender)) {
                curRank++;
                if (curRank == rank) {
                    return rec.get(0);
                }
            }
        }
        
        return nameAtRank;
    }
    
    public void whatIsNameInYear (String name, int year, int newYear, String gender) {
        int nameRankAtBirth = 0;
        
        nameRankAtBirth = getRank(year, name, gender);
        
        System.out.println( name + ", if you were born in " + newYear + " your name would have been " +
                            getName(newYear, nameRankAtBirth, gender) + ".");
        
    }
    
    public int yearOfHighestRank (String name, String gender) {
        DirectoryResource dr = new DirectoryResource();
        int highestRank = 0;
        String highestRankedYear = "";
        Boolean found = false;
        
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            int curRank = getRank(name, gender, fr);
            
            if (curRank != -1) {
                if ( highestRank == 0 || curRank < highestRank ) {
                    highestRank = curRank;
                    highestRankedYear = f.getName().substring(f.getName().indexOf("yob")+3, 7);
                }
            }
        }
        
        System.out.println("Highest ranked year was " + highestRankedYear);
        return Integer.parseInt(highestRankedYear);
    }
    
    public double getAverageRank( String name, String gender ) {
        DirectoryResource dr = new DirectoryResource();
        double avgRank = -1.0;
        int found = 0;
        double value = 0;
        
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            int rank = getRank (name, gender, fr);
            
            if (rank > 0) {
                found++;
                value += rank;
            }
        }
        
        System.out.println("DEBUG: found(" + found + ") | value(" + value + ")");
        avgRank = value / found;
        
        return avgRank;
    }
    
    public int getTotalBirthsRankedHigher(int year, String name, String gender) {
        // If getTotalBirthsRankedHigher accesses the "yob2012short.csv" file with
        // name set to “Ethan”, gender set to “M”, and year set to 2012, then this
        // method should return 15, since Jacob has 8 births and Mason has 7 births,
        // and those are the only two ranked higher than Ethan
        
        // First, what rank is my name?
        int rank = getRank( year, name, gender );
        
        // Open the file for my birth year
        FileResource fr = new FileResource("us_babynames_by_year/yob" + year + ".csv");
        
        int count = 0;
        int curRank = 0;
        
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (rec.get(1).equals(gender)) {
                curRank++;
                if (curRank < rank) {
                    count += Integer.parseInt(rec.get(2));
                }
            }
        }
        
        return count;
    }
    
    public void testGetTotalBirthsHigher() {
        System.out.println( "There were " + getTotalBirthsRankedHigher( 1979, "Donald", "M" ) + " births ranked higher than Donald in 1979.");
    }
    
    public void testGetAverageRank() {
        System.out.println( getAverageRank( "Donald", "M" ) );
    }
    
    public void testYearOfHighestRank() {
        System.out.println(yearOfHighestRank( "Donald", "M" ));
    }
    
    public void testWhatIsNameInYear() {
        whatIsNameInYear( "Donald", 1979, 1922, "M" );
    }
    
    public void testTotalBirths() {
        FileResource fr = new FileResource("us_babynames_by_year/yob1979.csv");
        totalBirths(fr);
    }
    
    public void testGetRank() {
        System.out.println(getRank(1979, "Donald", "M"));
    }
    
    public void testGetName() {
        System.out.println("Get Name '1980', '350', 'F' = " + getName(1980, 350, "F"));
    }
}
