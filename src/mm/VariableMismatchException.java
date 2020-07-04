/**
 * File Name: MasterMind.java
 * @author Gregory McAdams
 * E-mail: gmcadams1@comcast.net
 * 
 * Description: Thrown when the Player's guess, or master code, has a 
 * different number of entries (variables) than of that specified.
 **/

package mm;

/**
 * @author Greg
 *
 */
public class VariableMismatchException extends Exception {

        /**
         * 
         */
        private static final long serialVersionUID = 5643583601782431160L;
        private String message;

        /**
         * @param args
         */
        public static void main(String[] args) {
                // TODO Auto-generated method stub

        }
        
        public VariableMismatchException(String message)
        {
                super();
                this.message = message;
        }

        public String getMessage()
        {
                return this.message;
        }
}