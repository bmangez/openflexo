/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.netbeans.lib.cvsclient.command.export;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.command.KeywordSubstitutionOptions;
import org.netbeans.lib.cvsclient.command.RepositoryCommand;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.MessageEvent;
import org.netbeans.lib.cvsclient.request.ArgumentRequest;
import org.netbeans.lib.cvsclient.request.CommandRequest;
import org.netbeans.lib.cvsclient.request.DirectoryRequest;

/**
 * The export command exports the projects (modules in the repository)
 * to the local directory structure.
 *
 * @author  MIlos Kleint
 */
public class ExportCommand extends RepositoryCommand {

    /**
     * A store of potentially empty directories. When a directory has a file
     * in it, it is removed from this set. This set allows the prune option
     * to be implemented.
     */
    private final Set emptyDirectories = new HashSet();
    private boolean pruneDirectories;
    private KeywordSubstitutionOptions keywordSubstitutionOptions;

    /** Holds value of property exportByDate. */
    private String exportByDate;

    /** Holds value of property exportByRevision. */
    private String exportByRevision;

    /** Holds value of property exportDirectory. */
    private String exportDirectory;

    /** Holds value of property useHeadIfNotFound. */
    private boolean useHeadIfNotFound;

    /** Don't shorten module paths if -d specified. */
    private boolean notShortenPaths;
    
    /** Do not run module program (if any). */
    private boolean notRunModuleProgram;
    
    public ExportCommand() {
        resetCVSCommand();
    }

    /**
     * Returns the keyword substitution option.
     */
    public KeywordSubstitutionOptions getKeywordSubstitutionOptions() {
        return keywordSubstitutionOptions;
    }

    /**
     * Sets the keywords substitution option.
     */
    public void setKeywordSubstitutionOptions(KeywordSubstitutionOptions keywordSubstitutionOptions) {
        this.keywordSubstitutionOptions = keywordSubstitutionOptions;
    }

    /**
     * Set whether to prune directories.
     * This is the -P option in the command-line CVS.
     */
    public void setPruneDirectories(boolean pruneDirectories) {
        this.pruneDirectories = pruneDirectories;
    }

    /**
     * Get whether to prune directories.
     * @return true if directories should be removed if they contain no files,
     * false otherwise.
     */
    public boolean isPruneDirectories() {
        return pruneDirectories;
    }

    /**
     * Execute this command
     * @param client the client services object that provides any necessary
     * services to this command, including the ability to actually process
     * all the requests
     */
    @Override
	protected void postExpansionExecute(ClientServices client, EventManager em)
            throws CommandException, AuthenticationException {
        //
        // moved modules code to the end of the other arguments --GAR
        //
        final int FIRST_INDEX = 0;
        final int SECOND_INDEX = 1;
        if (!isRecursive()) {
            requests.add(FIRST_INDEX, new ArgumentRequest("-l")); //NOI18N
        }
        if (useHeadIfNotFound) {
            requests.add(FIRST_INDEX, new ArgumentRequest("-f")); //NOI18N
        }
        if (exportDirectory != null && (!exportDirectory.equals(""))) {
            requests.add(FIRST_INDEX, new ArgumentRequest("-d")); //NOI18N
            requests.add(SECOND_INDEX, new ArgumentRequest(getExportDirectory()));
        }
        if (exportByDate != null && exportByDate.length() > 0) {
            requests.add(FIRST_INDEX, new ArgumentRequest("-D")); //NOI18N
            requests.add(SECOND_INDEX, new ArgumentRequest(getExportByDate()));
        }
        if (exportByRevision != null && exportByRevision.length() > 0) {
            requests.add(FIRST_INDEX, new ArgumentRequest("-r")); //NOI18N
            requests.add(SECOND_INDEX, new ArgumentRequest(getExportByRevision()));
        }
        if (notShortenPaths) {
            requests.add(FIRST_INDEX, new ArgumentRequest("-N")); //NOI18N
        }
        if (notRunModuleProgram) {
            requests.add(FIRST_INDEX, new ArgumentRequest("-n")); //NOI18N
        }
        if (getKeywordSubstitutionOptions() != null) {
            requests.add(new ArgumentRequest("-k" + getKeywordSubstitutionOptions())); //NOI18N
        }

        addArgumentRequests();

        requests.add(new DirectoryRequest(".", client.getRepository())); //NOI18N
        requests.add(CommandRequest.EXPORT);
        try {
            client.processRequests(requests);
            if (pruneDirectories) {
                pruneEmptyDirectories();
            }
            requests.clear();

        }
        catch (CommandException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new CommandException(ex, ex.getLocalizedMessage());
        } finally {
            removeAllCVSAdminFiles();
        }
    }

    private void removeAllCVSAdminFiles() {
        File rootDirect = null;
        if (getExportDirectory() != null) {
            rootDirect = new File(getLocalDirectory(), getExportDirectory());
            deleteCVSSubDirs(rootDirect);
        }
        else {
            rootDirect = new File(getLocalDirectory());
            Iterator mods = expandedModules.iterator();
            while (mods.hasNext()) {
                String mod = mods.next().toString();
                File modRoot = new File(rootDirect.getAbsolutePath(), mod);
                deleteCVSSubDirs(modRoot);
            }
        }
    }

    private void deleteCVSSubDirs(File root) {
        if (root.isDirectory()) {
            File[] subDirs = root.listFiles();
            if (subDirs == null) {
                return;
            }

            for (int i = 0; i < subDirs.length; i++) {
                if (subDirs[i].isDirectory()) {
                    if (subDirs[i].getName().equalsIgnoreCase("CVS")) { //NOI18N
                        final File[] adminFiles = subDirs[i].listFiles();
                        for (int j = 0; j < adminFiles.length; j++) {
                            adminFiles[j].delete();
                        }
                        subDirs[i].delete();
                    }
                    else {
                        deleteCVSSubDirs(subDirs[i]);
                    }
                }
            }
        }
    }

    @Override
	public String getCVSCommand() {
        StringBuffer toReturn = new StringBuffer("export "); //NOI18N
        toReturn.append(getCVSArguments());
        if (modules != null && modules.size() > 0) {
            for (Iterator it = modules.iterator(); it.hasNext();) {
                String module = (String)it.next();
                toReturn.append(module);
                toReturn.append(' ');
            }
        }
        else {
            String localizedMsg = CommandException.getLocalMessage("ExportCommand.moduleEmpty.text"); //NOI18N
            toReturn.append(" "); //NOI18N
            toReturn.append(localizedMsg);
        }
        return toReturn.toString();
    }

    @Override
	public String getCVSArguments() {
        StringBuffer toReturn = new StringBuffer(""); //NOI18N
        if (!isRecursive()) {
            toReturn.append("-l "); //NOI18N
        }
        if (isUseHeadIfNotFound()) {
            toReturn.append("-f "); //NOI18N
        }
        if (getExportByDate() != null) {
            toReturn.append("-D "); //NOI18N
            toReturn.append(getExportByDate());
            toReturn.append(" "); //NOI18N
        }
        if (getExportByRevision() != null) {
            toReturn.append("-r "); //NOI18N
            toReturn.append(getExportByRevision());
            toReturn.append(" "); //NOI18N
        }
        if (isPruneDirectories()) {
            toReturn.append("-P "); //NOI18N
        }
        if (isNotShortenPaths()) {
            toReturn.append("-N "); // NOI18N
        }
        if (isNotRunModuleProgram()) {
            toReturn.append("-n "); // NOI18N
        }
        if (getExportDirectory() != null) {
            toReturn.append("-d "); //NOI18N
            toReturn.append(getExportDirectory());
            toReturn.append(" "); //NOI18N
        }
        if (getKeywordSubstitutionOptions() != null) {
            toReturn.append("-k"); //NOI18N
            toReturn.append(getKeywordSubstitutionOptions().toString());
            toReturn.append(" "); //NOI18N
        }
        return toReturn.toString();
    }

    @Override
	public boolean setCVSCommand(char opt, String optArg) {
        if (opt == 'k') {
            setKeywordSubstitutionOptions(KeywordSubstitutionOptions.findKeywordSubstOption(optArg));
        }
        else if (opt == 'r') {
            setExportByRevision(optArg);
        }
        else if (opt == 'f') {
            setUseHeadIfNotFound(true);
        }
        else if (opt == 'D') {
            setExportByDate(optArg);
        }
        else if (opt == 'd') {
            setExportDirectory(optArg);
        }
        else if (opt == 'P') {
            setPruneDirectories(true);
        }
        else if (opt == 'N') {
            setNotShortenPaths(true);
        }
        else if (opt == 'n') {
            setNotRunModuleProgram(true);
        }
        else if (opt == 'l') {
            setRecursive(false);
        }
        else if (opt == 'R') {
            setRecursive(true);
        }
        else {
            return false;
        }
        return true;
    }

    @Override
	public void resetCVSCommand() {
        setModules(null);
        setKeywordSubstitutionOptions(null);
        setPruneDirectories(false);
        setRecursive(true);
        setExportByDate(null);
        setExportByRevision(null);
        setExportDirectory(null);
        setUseHeadIfNotFound(false);
        setNotShortenPaths(false);
        setNotRunModuleProgram(false);
    }

    @Override
	public String getOptString() {
        return "k:r:D:NPlRnd:f"; //NOI18N
    }

    /**
     * Creates the ExportBuilder.
     */
    @Override
	public Builder createBuilder(EventManager eventManager) {
        return new ExportBuilder(eventManager, this);
    }

    /**
     * Called when the server wants to send a message to be displayed to
     * the user. The message is only for information purposes and clients
     * can choose to ignore these messages if they wish.
     * @param e the event
     */
    @Override
	public void messageSent(MessageEvent e) {
        super.messageSent(e);
        // we use this event to determine which directories need to be checked
        // for updating
        if (pruneDirectories &&
                e.getMessage().indexOf(": Exporting ") > 0) { //NOI18N
            File file = new File(getLocalDirectory(), e.getMessage().substring(21));
            emptyDirectories.add(file);
        }
    }

    /**
     * Prunes a directory, recursively pruning its subdirectories
     * @param directory the directory to prune
     */
    private boolean pruneEmptyDirectory(File directory) throws IOException {
        boolean empty = true;

        final File[] contents = directory.listFiles();

        // should never be null, but just in case...
        if (contents != null) {
            for (int i = 0; i < contents.length; i++) {
                if (contents[i].isFile()) {
                    empty = false;
                }
                else {
                    if (!contents[i].getName().equals("CVS")) { //NOI18N
                        empty = pruneEmptyDirectory(contents[i]);
                    }
                }

                if (!empty) {
                    break;
                }
            }

            if (empty) {
                // check this is a CVS directory and not some directory the user
                // has stupidly called CVS...
                final File entriesFile = new File(directory, "CVS/Entries"); //NOI18N
                if (entriesFile.exists()) {
                    final File adminDir = new File(directory, "CVS"); //NOI18N
                    final File[] adminFiles = adminDir.listFiles();
                    for (int i = 0; i < adminFiles.length; i++) {
                        adminFiles[i].delete();
                    }
                    adminDir.delete();
                    directory.delete();
                }
            }
        }

        return empty;
    }

    /**
     * Remove any directories that don't contain any files
     */
    private void pruneEmptyDirectories() throws IOException {
        final Iterator it = emptyDirectories.iterator();
        while (it.hasNext()) {
            final File dir = (File)it.next();
            // we might have deleted it already (due to recursive delete)
            // so we need to check existence
            if (dir.exists()) {
                pruneEmptyDirectory(dir);
            }
        }
        emptyDirectories.clear();
    }

    /** Getter for property exportByDate.
     * @return Value of property exportByDate.
     */
    public String getExportByDate() {
        return this.exportByDate;
    }

    /** Setter for property exportByDate.
     * @param exportByDate New value of property exportByDate.
     */
    public void setExportByDate(String exportByDate) {
        this.exportByDate = exportByDate;
    }

    /** Getter for property exportByRevision.
     * @return Value of property exportByRevision.
     */
    public String getExportByRevision() {
        return this.exportByRevision;
    }

    /** Setter for property exportByRevision.
     * @param exportByRevision New value of property exportByRevision.
     */
    public void setExportByRevision(String exportByRevision) {
        this.exportByRevision = exportByRevision;
    }

    /** Getter for property exportDirectory.
     * @return Value of property exportDirectory.
     */
    public String getExportDirectory() {
        return this.exportDirectory;
    }

    /** Setter for property exportDirectory.
     * @param exportDirectory New value of property exportDirectory.
     */
    public void setExportDirectory(String exportDirectory) {
        this.exportDirectory = exportDirectory;
    }

    /** Getter for property useHeadIfNotFound.
     * @return Value of property useHeadIfNotFound.
     */
    public boolean isUseHeadIfNotFound() {
        return this.useHeadIfNotFound;
    }

    /** Setter for property useHeadIfNotFound.
     * @param useHeadIfNotFound New value of property useHeadIfNotFound.
     */
    public void setUseHeadIfNotFound(boolean useHeadIfNotFound) {
        this.useHeadIfNotFound = useHeadIfNotFound;
    }

    /**
     * Getter for property notShortenPaths.
     * @return Value of property notShortenPaths.
     */
    public boolean isNotShortenPaths() {
        return notShortenPaths;
    }
    
    /**
     * Setter for property notShortenPaths.
     * @param notShortenPaths New value of property notShortenPaths.
     */
    public void setNotShortenPaths(boolean notShortenPaths) {
        this.notShortenPaths = notShortenPaths;
    }
    
    /**
     * Getter for property notRunModuleProgram.
     * @return Value of property notRunModuleProgram.
     */
    public boolean isNotRunModuleProgram() {
        return notRunModuleProgram;
    }
    
    /**
     * Setter for property notRunModuleProgram.
     * @param notRunModuleProgram New value of property notRunModuleProgram.
     */
    public void setNotRunModuleProgram(boolean notRunModuleProgram) {
        this.notRunModuleProgram = notRunModuleProgram;
    }
    
}
