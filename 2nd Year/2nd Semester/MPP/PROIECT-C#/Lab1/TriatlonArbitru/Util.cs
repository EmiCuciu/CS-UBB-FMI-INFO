using System.Windows.Forms;
using log4net;

namespace TriatlonArbitru
{
    public static class Util
    {
        private static readonly ILog logger = LogManager.GetLogger(typeof(Util));

        public static void ShowWarning(string header, string content)
        {
            MessageBox.Show(content, header, MessageBoxButtons.OK, MessageBoxIcon.Information);
        }
    }
}